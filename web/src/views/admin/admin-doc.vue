<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-row :gutter="24">
          <a-col :span="8">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleQuery()">
                    Search
                  </a-button>
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" @click="add()">
                    Add
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-table
                v-if="level1.length > 0"
                :columns="columns"
                :row-key="record => record.id"
                :data-source="level1"
                :loading="loading"
                :pagination="false"
                size="small"
                :defaultExpandAllRows="true"
            >
              <template #name="{ text, record }">
                {{record.sort}}, {{text}}
              </template>
              <template v-slot:action="{ text, record }">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    Edit
                  </a-button>
                  <a-popconfirm
                      title="Are you sure?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="dashed" size="small">
                      Delete
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>
          </a-col>
          <a-col :span="16">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    Save
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-form :model="doc" layout="vertical">
              <a-form-item>
                <a-input v-model:value="doc.name" placeholder="Name"/>
              </a-form-item>
              <a-form-item>
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="Select a parent document"
                    tree-default-expand-all
                    :fieldNames="{label: 'name', value: 'id'}"
                >
                </a-tree-select>
              </a-form-item>
              <a-form-item>
                <a-input v-model:value="doc.sort " placeholder="Order" />
              </a-form-item>
              <a-form-item>
                <div id="content"></div>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
<!--  <a-modal v-model:open="modalVisible" title="Doc form"-->
<!--           :confirm-loading="modalLoading"-->
<!--           @ok="modalHandleOk">-->
<!--  </a-modal>-->
</template>

<script lang="ts">


import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from "ant-design-vue";
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor';
import { nextTick } from 'vue';

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};
    const loading = ref(false);
    const docs = ref([]);
    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * Level-one Tree，children are level-two
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref();
    level1.value = [];

    /**
     * Data query
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("Original data:", docs.value);
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("Tree-Structured data:", level1);
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * --------Form----------
     */
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref();
    doc.value = {};
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    let editor : any;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value ).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data == commonResp

        if (data.success) {
          modalVisible.value = false;

          //load form again
          handleQuery();
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * Set a node and its children to disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // Traverse all the nodes at a certain level
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // Current node is the target
          // Set the target node to disabled
          node.disabled = true;

          // Traverse all children nodes, disable them all
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // If the current node is not our target, look into its children
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    /**
     * Get all the target node and its children nodes for the deletion
     */
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // Traverse all the nodes at a certain level
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // Current node is the target
          ids.push(id);

          // Traverse all children nodes, delete them all
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // If the current node is not our target, look into its children
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    // ---Edit---
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      // Prevent selecting the current node or its descendants as parent (would break the tree)
      treeSelectData.value = Tool.copy(level1.value);           // Make a deep copy of the tree
      setDisable(treeSelectData.value, record.id);              // Disable current node & its descendants

      // Add a "None" option at the top
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    // ---Add---
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);

      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    const handleDelete = (id : number) => {
      getDeleteIds(level1.value, id);
      axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
        const data = response.data; // data == commonResp

        if (data.success) {
          //load form again
          handleQuery();
        }
      });
    };


    onMounted(() => {
      handleQuery( );
      nextTick(() => {
        editor = new E('#content');
        editor.config.zIndex = 0;
        editor.create();
      });
    });

    return {
      param,
      level1,
      columns,
      loading,
      handleQuery,

      edit,
      add,
      handleDelete,

      doc,
      modalVisible,
      modalLoading,
      handleSave,

      treeSelectData,
    }
  }
});
</script>