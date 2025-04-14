<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-input v-model:value="param.name" placeholder="Name">
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
            :columns="columns"
            :row-key="record => record.id"
            :data-source="ebooks"
            :pagination="pagination"
            :loading="loading"
            @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
          </template>
          <template v-slot:category="{text, record}">
            <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
          </template>
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                Edit
              </a-button>
              <a-popconfirm
                  title="Are you sure?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="dashed">
                  Delete
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
  <a-modal v-model:open="modalVisible" title="Ebook form"
           :confirm-loading="modalLoading"
           @ok="modalHandleOk">

    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Cover">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="Category">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>
      <a-form-item label="Description">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">


import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from "ant-design-vue";
import {Tool} from "@/utils/tool";


export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
     const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);
    const ebooks = ref([]);
    const columns = [
      {
        title: 'cover',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'Category',
        slots: { customRender: 'category' },
      },
      {
        title: 'Number of Documents',
        dataIndex: 'docCount'
      },
      {
        title: 'Number of Views',
        dataIndex: 'viewCount'
      },
      {
        title: 'Number of Likes',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * Data query
     **/
    const handleQuery = (p: any) => {
      loading.value = true;
      // Empty the current value or after the edition we still see the old data
      // ebooks.value = [];
      axios.get("/ebook/list", {
        params: {
          page: p.page,
          size: p.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = p.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    /**
     * --------Form----------
     */
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const modalHandleOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value ).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data == commonResp

        if (data.success) {
          modalVisible.value = false;

          //load form again
          handleQuery({
            // These two parameters' name must match the ones in PageReq
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }

      });
    };

    // ---Edit---
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    // ---Add---
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    const handleDelete = (id : number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        console.log("Deleting ID:", id);
        const data = response.data; // data == commonResp

        if (data.success) {
          //load form again
          handleQuery({
            // These two parameters' name must match the ones in PageReq
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    const level1 = ref();
    let categorys: any;
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("Original data:", categorys);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("Tree-Structured data:", level1);

          // Load the ebooks, after loading the categories, or may have render problem
          handleQuery({
            // These two parameters' name must match the ones in PageReq
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }

      });
    };

    const getCategoryName = (cid : number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id == cid) {
          result = item.name;
        }
      });

      return result;
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      param,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      getCategoryName,

      edit,
      add,
      handleDelete,

      ebook,
      modalVisible,
      modalLoading,
      modalHandleOk,
      categoryIds,
      level1,
    }
  }
});
</script>