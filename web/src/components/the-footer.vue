<template>
  <a-layout-footer style="text-align: center">
    Wiki Ebook ©2025 Created by Vincent Guo
    <p><span v-show="user.id"> User: {{user.name}} </span></p>
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);

    return {user}
  }
});
</script>