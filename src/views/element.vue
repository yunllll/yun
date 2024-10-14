<template>
    <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
        <el-table-column prop="date" label="Date" width="180" />
        <el-table-column prop="name" label="Name" width="180" />
        <el-table-column prop="address" label="Address" />
    </el-table>
    <!-- <VueUiRadar :data="chartData" /> -->
    <div ref="dplayerRef"></div>
</template>
<script setup>
import { VueUiRadar } from "vue-data-ui";
import { ref, onMounted } from 'vue';
import DPlayer from 'dplayer';
const dplayerRef = ref(null);
onMounted(() => {
    const dp = new DPlayer({
        container: dplayerRef.value,
        autoplay: false,
        theme: '#FADFA3',
        loop: true,
        lang: 'zh-cn',
        screenshot: true,
        hotkey: true,
        preload: 'auto',
        logo: 'logo.png',
        volume: 0.7,
        mutex: true,
        video: {
            url: 'dplayer.mp4',
            pic: 'dplayer.png',
            thumbnails: 'thumbnails.jpg',
            type: 'auto',
        },
        subtitle: {
            url: 'dplayer.vtt',
            type: 'webvtt',
            fontSize: '25px',
            bottom: '10%',
            color: '#b7daff',
        },
        danmaku: {
            id: '9E2E3368B56CDBB4',
            api: 'https://api.prprpr.me/dplayer/',
            token: 'tokendemo',
            maximum: 1000,
            addition: ['https://api.prprpr.me/dplayer/v3/bilibili?aid=4157142'],
            user: 'DIYgod',
            bottom: '15%',
            unlimited: true,
            speedRate: 0.5,
        },
        contextmenu: [
            {
                text: 'custom1',
                link: 'https://github.com/DIYgod/DPlayer',
            },
            {
                text: 'custom2',
                click: (player) => {
                    console.log(player);
                },
            },
        ],
        highlight: [
            {
                text: 'marker for 20s',
                time: 20,
            },
            {
                text: 'marker for 2mins',
                time: 120,
            },
        ],
    });
});


const tableRowClassName = ({ row, rowIndex }) => {
    if (rowIndex === 1) {
        return 'warning-row';
    } else if (rowIndex === 3) {
        return 'success-row';
    }
    return '';
}

const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
];
</script>


<style>
.el-table .warning-row {
    --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

.el-table .success-row {
    --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>
<!-- import React from 'react';
import { Input, QRCode, Space } from 'antd';
const App = () => {
  const [text, setText] = React.useState('https://ant.design/');
  return (
    <Space direction="vertical" align="center">
      <QRCode value={text || '-'} />
      <Input
        placeholder="-"
        maxLength={60}
        value={text}
        onChange={(e) => setText(e.target.value)}
      />
    </Space>
  );
};
export default App; -->