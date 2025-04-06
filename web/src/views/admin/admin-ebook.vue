<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <p>
          <a-button type="primary" @click="add()" size="large">
            Add
          </a-button>
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
        <a-input v-model:value="ebook.category1Id" />
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

export default defineComponent({
  name: 'AdminEbook',
  setup() {
     const pagination = ref({
      current: 1,
      pageSize: 4,
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
        title: 'category',
        slots: { customRender: 'category' },
        dataIndex: 'category1Id'
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
      axios.get("/ebook/list", {
        params: {
          page: p.page,
          size: p.size
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
    const ebook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const modalHandleOk = () => {
      modalLoading.value = true;
      axios.post("/ebook/save", ebook.value ).then((response) => {
        const data = response.data; // data == commonResp

        if (data.success) {
          modalLoading.value = false;
          modalVisible.value = false;

          //load form again
          handleQuery({
            // These two parameters' name must match the ones in PageReq
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    // ---Edit---
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = record;
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


    onMounted(() => {
      handleQuery({
        // These two parameters' name must match the ones in PageReq
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      add,
      handleDelete,

      ebook,
      modalVisible,
      modalLoading,
      modalHandleOk
    }
  }
});
</script>