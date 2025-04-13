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
            :data-source="categorys"
            :pagination="pagination"
            :loading="loading"
            @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
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
  <a-modal v-model:open="modalVisible" title="Category form"
           :confirm-loading="modalLoading"
           @ok="modalHandleOk">

    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Name">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="Parent">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="Order">
        <a-input v-model:value="category.sort " type="textarea" />
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
  name: 'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
     const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);
    const categorys = ref([]);
    const columns = [
      {
        title: 'name',
        dataIndex: 'name'
      },
      {
        title: 'Parent Category',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: 'Order',
        dataIndex: 'sort'
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
      axios.get("/category/list", {
        params: {
          page: p.page,
          size: p.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content.list;

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
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const modalHandleOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value ).then((response) => {
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
      category.value = Tool.copy(record);
    };

    // ---Add---
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    const handleDelete = (id : number) => {
      axios.delete("/category/delete/" + id).then((response) => {
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


    onMounted(() => {
      handleQuery({
        // These two parameters' name must match the ones in PageReq
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      param,
      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,
      handleDelete,

      category,
      modalVisible,
      modalLoading,
      modalHandleOk
    }
  }
});
</script>