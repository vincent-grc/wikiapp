<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to="/admin/user">Manage User</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/admin/ebook">Manage Ebook</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to="/admin/category">Manage Category</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to="/about">About me</router-link>
      </a-menu-item>
      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="user.id">
          <span>退出登录</span>
        </a>
      </a-popconfirm>
      <a class="login-menu" v-show="user.id">
        <span>您好：{{user.name}}</span>
      </a>
      <a class="login-menu" v-show="!user.id" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="Login"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="Username">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="Password">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup () {
    // Save after login
    const user = computed(() => store.state.user);

    // Login test
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // Login
    const login = () => {
      console.log("Start logging");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("Login successfully！");

          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // Logout
    const logout = () => {
      console.log("Start logging out");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("Logout successfully！");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,
      logout
    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>