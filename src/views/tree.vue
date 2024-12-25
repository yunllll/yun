<template>
    <div>
      <canvas ref="canvas" width="400" height="600"></canvas>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  const canvas = ref(null);
  const snowflakes = ref([]);
  
  const initializeSnowflakes = () => {
    const snowflakeCount = 100; // 增加雪花数量
    for (let i = 0; i < snowflakeCount; i++) {
      snowflakes.value.push({
        x: Math.random() * 400,
        y: Math.random() * 600,
        radius: Math.random() * 3 + 2,
        speed: Math.random() * 1 + 0.5,
        opacity: Math.random() // 随机透明度
      });
    }
  };
  
  const drawChristmasTree = () => {
    const ctx = canvas.value.getContext('2d');
    drawTree(ctx);
  };
  
  const drawTree = (ctx) => {
    // 树干
    ctx.fillStyle = '#8B4513'; // 树干颜色
    ctx.fillRect(180, 450, 40, 50);
  
    // 树叶
    ctx.fillStyle = '#228B22'; // 树叶颜色
    ctx.beginPath();
    ctx.moveTo(200, 50);
    ctx.lineTo(100, 300);
    ctx.lineTo(300, 300);
    ctx.closePath();
    ctx.fill();
  
    ctx.beginPath();
    ctx.moveTo(200, 100);
    ctx.lineTo(120, 250);
    ctx.lineTo(280, 250);
    ctx.closePath();
    ctx.fill();
  
    ctx.beginPath();
    ctx.moveTo(200, 150);
    ctx.lineTo(140, 200);
    ctx.lineTo(260, 200);
    ctx.closePath();
    ctx.fill();
  
    // 绘制阴影
    ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
    ctx.fillRect(180, 500, 40, 10); // 树干阴影
    ctx.beginPath();
    ctx.moveTo(200, 50);
    ctx.lineTo(90, 300);
    ctx.lineTo(310, 300);
    ctx.closePath();
    ctx.fill(); // 树叶阴影
  
    // 装饰球
    drawOrnaments(ctx);
    
    // 绘制星星
    drawStar(ctx, 200, 30, 10, 5, 5, 'gold');
  };
  
  const drawOrnaments = (ctx) => {
    const colors = ['red', 'blue', 'yellow', 'gold', 'silver'];
    for (let i = 0; i < 10; i++) { // 增加装饰球数量
      ctx.fillStyle = colors[Math.floor(Math.random() * colors.length)];
      ctx.beginPath();
      ctx.arc(150 + Math.random() * 100, 200 + Math.random() * 100, 10, 0, Math.PI * 2);
      ctx.fill();
    }
  };
  
  const drawStar = (ctx, x, y, radius, points, inset) => {
    ctx.fillStyle = 'gold';
    ctx.beginPath();
    const angle = (Math.PI * 2) / points;
    for (let i = 0; i < points; i++) {
      ctx.lineTo(x + Math.cos(i * angle) * radius, y + Math.sin(i * angle) * radius);
      ctx.lineTo(x + Math.cos(i * angle + angle / 2) * (radius / inset), y + Math.sin(i * angle + angle / 2) * (radius / inset));
    }
    ctx.closePath();
    ctx.fill();
  };
  
  const drawSnowflakes = (ctx) => {
    snowflakes.value.forEach(snowflake => {
      ctx.fillStyle = `rgba(255, 255, 255, ${snowflake.opacity})`; // 根据透明度绘制雪花
      ctx.beginPath();
      ctx.arc(snowflake.x, snowflake.y, snowflake.radius, 0, Math.PI * 2);
      ctx.fill();
    });
  };
  
  const animateSnowflakes = () => {
    const ctx = canvas.value.getContext('2d');
  
    const animate = () => {
      ctx.clearRect(0, 0, canvas.value.width, canvas.value.height); // 清空画布
      drawChristmasTree(); // 重新绘制圣诞树
      snowflakes.value.forEach(snowflake => {
        snowflake.y += snowflake.speed; // 更新雪花位置
        if (snowflake.y > canvas.value.height) {
          snowflake.y = 0; // 如果雪花超出画布，重置到顶部
          snowflake.x = Math.random() * canvas.value.width; // 随机x位置
          snowflake.opacity = Math.random(); // 随机透明度
        }
      });
      drawSnowflakes(ctx); // 绘制雪花
      requestAnimationFrame(animate); // 循环动画
    };
  
    animate();
  };
  
  onMounted(() => {
    initializeSnowflakes();
    drawChristmasTree();
    animateSnowflakes();
  });
  </script>
  
  <style scoped>
  canvas {
    background-color: #87CEEB; /* 天空蓝 */
    border: 1px solid #000;
  }
  </style>
  