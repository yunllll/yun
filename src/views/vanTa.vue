<template>
    <div ref="login" class="flex flex-justify-center flex-items-center h-100vh">
        <!-- 表单 -->
    </div>
</template>
 
<script setup>

import { ref, onMounted, onUnmounted } from "vue";
import * as THREE from "three";
import CLOUDS from "vanta/dist/vanta.clouds.min";

const login = ref();
let vantaEffect;

onMounted(() => {
    vantaEffect = CLOUDS({
        el: login.value,
        THREE: THREE,
        mouseControls: true,
        touchControls: true,
        gyroControls: false,
        minHeight: 530.00,
        minWidth: 910.00
    });
});

onUnmounted(() => {
    if (vantaEffect) {
        vantaEffect.destroy();
    }
});
</script>
 
<style scoped></style>