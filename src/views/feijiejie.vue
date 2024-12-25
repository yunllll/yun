<template>
    <button class="futuristic-button" style="margin-bottom: 80px;">基于yolov11的肺结节检测平台</button>
    <div>
        <div style="display: flex;justify-content: center;margin:0 200px;">
            <button type="file" @click="triggerFileInput" class="futuristic-button" style="margin-right: 100px;">{{
                fileName
                || '选择文件' }}</button>
            <input type="file" ref="fileInput" @change="onFileChangeI" style="display: none;" />
            <p v-if="fileName"></p>
            <button @click="uploadImage" class="futuristic-button" style="margin-right: 100px;">上传图片</button>
            <button @click="trainModel" class="futuristic-button">模型训练</button>
            <p v-if="trainingMessage">{{ trainingMessage }}</p>
            <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
        </div>
        <div v-if="result" style="display: flex; flex-direction: row;">
            <div style="display: flex;flex-direction: column;">
                <h3>原始图片:</h3>
                <img :src="result.imageUrl" alt="原始图片" />
            </div>
            <div style="display: flex;flex-direction: column;margin-left: 20px;">
                <h3>预测结果:</h3>
                <img :src="result.resultImageUrl" alt="未检测到肺结节" />
            </div>
            <div class="result-container">
                <pre class="result-text">
                     <span v-html="formattedPrediction"></span>
                </pre>
            </div>
        </div>
    </div>
    <button id="button">
                <p id="to-launch">BEST模型训练结果</p>
    </button>
    <div style="display: flex;justify-content: space-between;align-items: center;">
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxF1分数曲线</h3>
        <img :src="'/train/BoxF1_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxPression曲线</h3>
        <img :src="'/train/BoxP_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxPR曲线</h3>
        <img :src="'/train/BoxPR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxRecall曲线</h3>
        <img :src="'/train/BoxR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskF1分数曲线</h3>
        <img :src="'/train/MaskF1_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskPression曲线</h3>
        <img :src="'/train/MaskP_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskR曲线</h3>
        <img :src="'/train/MaskR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskPR曲线</h3>
        <img :src="'/train/MaskPR_curve.png'" alt="6666" />
        </div>
    </div>
    <div style="display: flex;justify-content: space-between;align-items: center;">
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>Label</h3>
        <img :src="'/train/labels.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>results</h3>
        <img :src="'/train/results.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>train_batch</h3>
        <img :src="'/train/train_batch15662.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val_batch_labels</h3>
        <img :src="'/train/val_batch0_labels.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val_batch_pred</h3>
        <img :src="'/train/val_batch0_pred.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>train混淆矩阵</h3>
        <img :src="'/train/confusion_matrix.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>train混淆矩阵_归一化</h3>
        <img :src="'/train/confusion_matrix_normalized.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>train_acc</h3>
        <p>0.72</p>
        </div>
    </div>
    <div v-if="result" style="margin-top: 40px;">
        <img src="/train.jpg" alt="">
    </div>
    <!-- <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 500px;">
        <h3>PR曲线:</h3>
        <img :src="result.resultPRUrl" alt="" />
    </div> -->
    <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 400px;">
        <h3>检测分割肺结核区域图:</h3>
        <img :src="result.resultCropUrl" alt="未检测到" />
    </div>
    <div style="display: flex;  flex-direction: column; align-items: center; padding-top: 8vh;position: relative;">
        <div class="card" style="position: absolute;top: 80px;left: 80px;width: 300px;height: 300px;">
            <div class="image"><img src="../assets/AnKe.jpg" alt="" style="width: 298px;height: 298px;"></div>
            <span class="title" style="text-align: center;">今天也要加油！</span>
        </div>
        <div class="card" style="position: absolute;top: 80px;right: 100px;width: 200px;height: 300px;">
            <div class="image"><img src="/chillguy.png" alt="" style="width: 200px;height: 298px;"></div>
            <span class="title" style="text-align: center;">I don't care......</span>
        </div>
        <!-- <div style="display: flex;justify-content: space-between;margin-bottom: 20px;">
            <input type="file" @change="onFileChange" accept="video/*" class="s" />
            <input type="file" @change="onFileChange1" accept="video/*" class="s" style="margin-left: 200px;" />
        </div>
        <div style="display: flex;justify-content: space-between;">
            <div ref="dplayerRef1" style="width: 500px; height: 300px;"></div>
            <div ref="dplayerRef2" style='width:500px;height:300px;margin-left: 40px;'></div>
        </div> -->
        <div class="container noselect" @click="resetData">

            <label for="checkbox" id="clickHandler"></label>
            <input type="checkbox" id="checkbox">

            <button id="button">
                <p id="to-launch">肺结节检测</p>
                <p id="tag">检测完成</p>
                <div id="platform"></div>
                <div class="caution">
                    <div id="caution-left">LAUNCH ZONE</div>
                    <div id="caution-right">LAUNCH ZONE</div>
                </div>
            </button>

            <p id="to-hover">Hover！</p>
            <div id="shuttle-wrapper">

                <div id="shadow"></div>
                <svg viewBox="0 0 230.24 542.46" xmlns:xlink="http://www.w3.org/1999/xlink"
                    xmlns="http://www.w3.org/2000/svg" id="b">
                    <defs>
                        <linearGradient gradientUnits="userSpaceOnUse"
                            gradientTransform="translate(160.35 -471.85) rotate(-7.06) scale(1.56 1.68)" y2="549.77"
                            x2="-104.11" y1="545.09" x1="-130.63" id="d">
                            <stop stop-color="#c6c6c6" offset="0"></stop>
                            <stop stop-color="#b1b1b1" offset="0"></stop>
                            <stop stop-color="#858585" offset=".02"></stop>
                            <stop stop-color="#606060" offset=".04"></stop>
                            <stop stop-color="#424242" offset=".05"></stop>
                            <stop stop-color="#2a2a2a" offset=".07"></stop>
                            <stop stop-color="#191919" offset=".09"></stop>
                            <stop stop-color="#101010" offset=".1"></stop>
                            <stop stop-color="#0d0d0d" offset=".13"></stop>
                            <stop stop-color="#454545" offset=".31"></stop>
                            <stop stop-color="#4c4c4c" offset=".33"></stop>
                            <stop stop-color="#484848" offset=".35"></stop>
                            <stop stop-color="#2b2c2b" offset=".52"></stop>
                            <stop stop-color="#171817" offset=".68"></stop>
                            <stop stop-color="#0b0c0b" offset=".84"></stop>
                            <stop stop-color="#070807" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="479.08" x2="113.37" y1="475.4" x1="70.56"
                            id="e">
                            <stop stop-opacity="0" stop-color="#fff" offset="0"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".32"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".38"></stop>
                            <stop stop-opacity="0" stop-color="#b3511b" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="270.33" x2="123.89" y1="269.31" x1="65.74"
                            id="f">
                            <stop stop-color="#e5e5e5" offset="0"></stop>
                            <stop stop-color="#e3e3e3" offset=".63"></stop>
                            <stop stop-color="#dcdcdc" offset=".85"></stop>
                            <stop stop-color="#d3d2d3" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="114.99" x2="112.45" y1="114.99" x1="70.84"
                            id="g">
                            <stop stop-color="#1a1a1a" offset="0"></stop>
                            <stop stop-color="#2a2a2a" offset=".05"></stop>
                            <stop stop-color="#434343" offset=".15"></stop>
                            <stop stop-color="#4d4d4d" offset=".2"></stop>
                            <stop stop-color="#474747" offset=".3"></stop>
                            <stop stop-color="#383838" offset=".69"></stop>
                            <stop stop-color="#333" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="142.07" x2="112.45" y1="142.07" x1="70.84" id="h">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="178.73" x2="112.45" y1="178.73" x1="70.84" id="i">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="254.03" x2="112.45" y1="254.03" x1="70.84" id="j">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="329.25" x2="112.45" y1="329.25" x1="70.84" id="k">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="386.88" x2="112.45" y1="386.88" x1="70.84" id="l">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="407.2" x2="112.45" y1="407.2" x1="70.84" id="m">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="414.79" x2="112.45" y1="414.79" x1="70.84" id="n">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="423.88" x2="112.45" y1="423.88" x1="70.84" id="o">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="435.68" x2="112.45" y1="435.68" x1="70.84" id="p">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="446.33" x2="112.45" y1="446.33" x1="70.84" id="q">
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="280.59" x2="112.45" y1="280.59" x1="70.67"
                            id="r">
                            <stop stop-opacity="0" stop-color="#fff" offset="0"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".14"></stop>
                            <stop stop-opacity=".03" stop-color="#fff" offset=".19"></stop>
                            <stop stop-opacity=".08" stop-color="#fff" offset=".23"></stop>
                            <stop stop-opacity=".15" stop-color="#fff" offset=".25"></stop>
                            <stop stop-opacity=".23" stop-color="#fff" offset=".28"></stop>
                            <stop stop-opacity=".34" stop-color="#fff" offset=".3"></stop>
                            <stop stop-opacity=".47" stop-color="#fff" offset=".32"></stop>
                            <stop stop-opacity=".61" stop-color="#fff" offset=".34"></stop>
                            <stop stop-opacity=".78" stop-color="#fff" offset=".36"></stop>
                            <stop stop-opacity=".8" stop-color="#fff" offset=".36"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".38"></stop>
                            <stop stop-opacity="0" stop-color="#b3511b" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="464.45" x2="120.43" y1="455.38" x1="63.17"
                            id="s">
                            <stop stop-opacity="0" stop-color="#fff" offset="0"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".15"></stop>
                            <stop stop-opacity=".03" stop-color="#fff" offset=".2"></stop>
                            <stop stop-opacity=".08" stop-color="#fff" offset=".24"></stop>
                            <stop stop-opacity=".15" stop-color="#fff" offset=".27"></stop>
                            <stop stop-opacity=".23" stop-color="#fff" offset=".29"></stop>
                            <stop stop-opacity=".34" stop-color="#fff" offset=".32"></stop>
                            <stop stop-opacity=".47" stop-color="#fff" offset=".34"></stop>
                            <stop stop-opacity=".61" stop-color="#fff" offset=".36"></stop>
                            <stop stop-opacity=".78" stop-color="#fff" offset=".38"></stop>
                            <stop stop-opacity=".8" stop-color="#fff" offset=".38"></stop>
                            <stop stop-opacity="0" stop-color="#fff" offset=".39"></stop>
                            <stop stop-color="#d3d3d3" offset=".72"></stop>
                            <stop stop-color="#d2d1d2" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#r" y2="91.43" x2="115.35" y1="87.78" x1="72.87" id="t">
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="217.5" x2="194.16" y1="217.5" x1="102.87"
                            id="v">
                            <stop stop-color="#d68029" offset="0"></stop>
                            <stop stop-color="#b3511b" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="404.51" x2="158.77" y1="23.53" x1="134.44"
                            id="w">
                            <stop stop-opacity=".4" stop-color="#dd884e" offset=".02"></stop>
                            <stop stop-opacity=".67" stop-color="#b76031" offset=".42"></stop>
                            <stop stop-color="#882e0d" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="162" x2="194.16" y1="162" x1="102.66" id="x">
                            <stop stop-color="#d8823a" offset="0"></stop>
                            <stop stop-color="#bf611e" offset="1"></stop>
                        </linearGradient>
                        <radialGradient gradientUnits="userSpaceOnUse"
                            gradientTransform="translate(119.12 192.29) rotate(-104.58) scale(1 .55)" r="134.05"
                            fy="71.95" fx="60.36" cy="64.81" cx="122.52" id="y">
                            <stop stop-opacity="0" stop-color="#dd884e" offset=".72"></stop>
                            <stop stop-color="#ed9654" offset="1"></stop>
                        </radialGradient>
                        <radialGradient xlink:href="#y"
                            gradientTransform="translate(176.37 200.83) rotate(-75.42) scale(1 -.55)" r="133.29"
                            fy="72.72" fx="68.57" cy="65.62" cx="130.39" id="z"></radialGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="217.56" x2="142.83" y1="438.65" x1="161.35"
                            id="aa">
                            <stop stop-color="#a52c14" offset="0"></stop>
                            <stop stop-opacity=".84" stop-color="#a53014" offset=".19"></stop>
                            <stop stop-opacity=".44" stop-color="#a73b14" offset=".59"></stop>
                            <stop stop-opacity="0" stop-color="#aa4814" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse"
                            gradientTransform="translate(189.08 .72) rotate(.18) scale(1.34 1)" y2="285.84" x2="-7.1"
                            y1="284.22" x1="-43.8" id="ab">
                            <stop stop-opacity="0" stop-color="#e65900" offset="0"></stop>
                            <stop stop-opacity=".03" stop-color="#e45800" offset=".13"></stop>
                            <stop stop-opacity=".12" stop-color="#de5502" offset=".29"></stop>
                            <stop stop-opacity=".27" stop-color="#d55104" offset=".46"></stop>
                            <stop stop-opacity=".48" stop-color="#c84b08" offset=".65"></stop>
                            <stop stop-opacity=".75" stop-color="#b7430c" offset=".84"></stop>
                            <stop stop-color="#a83c11" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#d" y2="591.23" x2="-38.94" y1="586.55" x1="-65.46" id="ac">
                        </linearGradient>
                        <linearGradient xlink:href="#e" y2="535.79" x2="222.95" y1="532.12" x1="180.14" id="ad">
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="327.04" x2="233.47" y1="326.03" x1="175.32"
                            id="ae">
                            <stop stop-color="#e5e5e5" offset="0"></stop>
                            <stop stop-color="#e5e5e5" offset=".04"></stop>
                            <stop stop-color="#e5e5e5" offset=".05"></stop>
                            <stop stop-color="#e3e3e3" offset=".63"></stop>
                            <stop stop-color="#dcdcdc" offset=".86"></stop>
                            <stop stop-color="#d3d2d3" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="463.91" x2="222.03" y1="463.91" x1="180.42" id="af">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="471.51" x2="222.03" y1="471.51" x1="180.42" id="ag">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="480.59" x2="222.03" y1="480.59" x1="180.42" id="ah">
                        </linearGradient>
                        <linearGradient xlink:href="#g" y2="492.4" x2="222.03" y1="492.4" x1="180.42" id="ai">
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="385.97" x2="222.04" y1="385.97" x1="180.43"
                            id="aj">
                            <stop stop-color="#d45300" offset="0"></stop>
                            <stop stop-color="#d35200" offset="0"></stop>
                            <stop stop-color="#995021" offset=".07"></stop>
                            <stop stop-color="#704e38" offset=".13"></stop>
                            <stop stop-color="#564d47" offset=".18"></stop>
                            <stop stop-color="#4d4d4d" offset=".2"></stop>
                            <stop stop-color="#474747" offset=".3"></stop>
                            <stop stop-color="#383838" offset=".69"></stop>
                            <stop stop-color="#333" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#aj" y2="443.6" y1="443.6" id="ak"></linearGradient>
                        <linearGradient xlink:href="#aj" y2="310.75" y1="310.75" id="al"></linearGradient>
                        <linearGradient xlink:href="#aj" y2="235.44" y1="235.44" id="am"></linearGradient>
                        <linearGradient xlink:href="#aj" y2="198.78" y1="198.78" id="an"></linearGradient>
                        <linearGradient xlink:href="#aj" y2="171.7" y1="171.7" id="ao"></linearGradient>
                        <linearGradient xlink:href="#g" y2="503.1" x2="222.03" y1="503.1" x1="180.42" id="ap">
                        </linearGradient>
                        <linearGradient xlink:href="#r" y2="337.31" x2="222.03" y1="337.31" x1="180.25" id="aq">
                        </linearGradient>
                        <linearGradient xlink:href="#s" y2="521.17" x2="230.01" y1="512.1" x1="172.75" id="ar">
                        </linearGradient>
                        <linearGradient xlink:href="#r" y2="148.14" x2="224.93" y1="144.49" x1="182.45" id="as">
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="424" x2="68.43" y1="414.65" x1="33.58"
                            id="at">
                            <stop stop-color="#e8e8e8" offset="0"></stop>
                            <stop stop-color="#f2f2f2" offset=".02"></stop>
                            <stop stop-color="#fafafa" offset=".04"></stop>
                            <stop stop-color="#fdfdfd" offset=".09"></stop>
                            <stop stop-color="#ededed" offset=".18"></stop>
                            <stop stop-color="#d3d3d3" offset=".38"></stop>
                            <stop stop-color="silver" offset=".58"></stop>
                            <stop stop-color="#b5b5b5" offset=".78"></stop>
                            <stop stop-color="#b2b2b2" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#d" gradientTransform="matrix(1, 0, 0, 1, 0, 0)" y2="466.58"
                            x2="111.3" y1="461.9" x1="84.78" id="au"></linearGradient>
                        <linearGradient xlink:href="#d" gradientTransform="translate(108.68 -1.95) rotate(13.28)"
                            y2="464.99" x2="78.06" y1="459.96" x1="49.55" id="av"></linearGradient>
                        <linearGradient xlink:href="#d" gradientTransform="translate(37.21 -2.14) rotate(4.64)"
                            y2="468.28" x2="84.28" y1="465.78" x1="70.1" id="aw"></linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="317.69" x2="185.62" y1="317.69" x1="17.4"
                            id="ax">
                            <stop stop-color="#d5d5d5" offset="0"></stop>
                            <stop stop-color="#d1d1d1" offset="1"></stop>
                        </linearGradient>
                        <filter filterUnits="userSpaceOnUse" id="ay">
                            <feOffset dy="-6" dx="13"></feOffset>
                            <feGaussianBlur stdDeviation="6" result="az"></feGaussianBlur>
                            <feFlood flood-opacity=".3" flood-color="#333"></feFlood>
                            <feComposite operator="in" in2="az"></feComposite>
                            <feComposite in="SourceGraphic"></feComposite>
                        </filter>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="307.1" x2="131.64" y1="302.93" x1="52.2"
                            id="ba">
                            <stop stop-color="#fff" offset="0"></stop>
                            <stop stop-color="#f9f9f9" offset=".06"></stop>
                            <stop stop-color="#f5f5f5" offset=".18"></stop>
                            <stop stop-color="#c7c7c7" offset=".37"></stop>
                            <stop stop-color="#c3c3c3" offset=".55"></stop>
                            <stop stop-color="#b9b9b9" offset=".7"></stop>
                            <stop stop-color="#b4b4b4" offset=".75"></stop>
                            <stop stop-color="#d3d3d3" offset=".94"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="427.46" x2="88.01" y1="180.46" x1="89.33"
                            id="bb">
                            <stop stop-color="#fff" offset="0"></stop>
                            <stop stop-opacity=".85" stop-color="#f7f8f8" offset=".07"></stop>
                            <stop stop-opacity=".65" stop-color="#eeefef" offset=".16"></stop>
                            <stop stop-opacity=".48" stop-color="#e5e7e7" offset=".26"></stop>
                            <stop stop-opacity=".33" stop-color="#dee0e1" offset=".37"></stop>
                            <stop stop-opacity=".21" stop-color="#d9dbdc" offset=".47"></stop>
                            <stop stop-opacity=".12" stop-color="#d4d7d8" offset=".58"></stop>
                            <stop stop-opacity=".05" stop-color="#d1d4d5" offset=".7"></stop>
                            <stop stop-opacity=".01" stop-color="#cfd2d3" offset=".83"></stop>
                            <stop stop-opacity="0" stop-color="#cfd2d3" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="413.58" x2="69.93" y1="523.99" x1="-.05"
                            id="bc">
                            <stop stop-color="#b8b7b7" offset="0"></stop>
                            <stop stop-color="#d1d0d0" offset=".4"></stop>
                            <stop stop-color="#fff" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#bc" y2="466.09" x2="44.69" y1="505.35" x1="19.81" id="bd">
                        </linearGradient>
                        <radialGradient gradientUnits="userSpaceOnUse"
                            gradientTransform="translate(261.63 273.14) rotate(125.14) scale(1 .88)" r="39.33"
                            fy="217.89" fx="66.91" cy="217.89" cx="66.91" id="be">
                            <stop stop-color="#565656" offset="0"></stop>
                            <stop stop-color="#484848" offset=".1"></stop>
                            <stop stop-color="#323232" offset=".3"></stop>
                            <stop stop-color="#2b2b2b" offset=".42"></stop>
                            <stop stop-color="#2b2b2b" offset=".51"></stop>
                            <stop stop-color="#2b2b2b" offset="1"></stop>
                        </radialGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="380.41" x2="126.25" y1="374.95" x1="129.28"
                            id="bf">
                            <stop stop-color="#fff" offset="0"></stop>
                            <stop stop-color="#fff" offset=".27"></stop>
                            <stop stop-color="#fff" offset=".32"></stop>
                            <stop stop-color="#fdfdfe" offset=".33"></stop>
                            <stop stop-color="#f6f8fd" offset=".33"></stop>
                            <stop stop-color="#eaeffc" offset=".33"></stop>
                            <stop stop-color="#d9e3f9" offset=".33"></stop>
                            <stop stop-color="#c2d2f6" offset=".34"></stop>
                            <stop stop-color="#a7bef3" offset=".34"></stop>
                            <stop stop-color="#86a6ee" offset=".34"></stop>
                            <stop stop-color="#628ae9" offset=".34"></stop>
                            <stop stop-color="#386ce4" offset=".34"></stop>
                            <stop stop-color="#376be4" offset=".34"></stop>
                            <stop stop-color="#3d6ade" offset=".68"></stop>
                            <stop stop-color="#dd4f38" offset=".69"></stop>
                            <stop stop-color="#ff4915" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="440.89" x2="97.61" y1="437.49" x1="65.52"
                            id="bg">
                            <stop stop-color="#e8e8e8" offset="0"></stop>
                            <stop stop-color="#f2f2f2" offset=".05"></stop>
                            <stop stop-color="#fafafa" offset=".13"></stop>
                            <stop stop-color="#fdfdfd" offset=".27"></stop>
                            <stop stop-color="#f2f2f2" offset=".36"></stop>
                            <stop stop-color="#c3c3c3" offset=".78"></stop>
                            <stop stop-color="#b2b2b2" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="198.33" x2="136.86" y1="176.68" x1="77.37"
                            id="bh">
                            <stop stop-color="#0e0a09" offset="0"></stop>
                            <stop stop-color="#0e0a09" offset="0"></stop>
                            <stop stop-color="#2a2828" offset=".08"></stop>
                            <stop stop-color="#3b3b3b" offset=".14"></stop>
                            <stop stop-color="#424242" offset=".19"></stop>
                            <stop stop-color="#303030" offset=".26"></stop>
                            <stop stop-color="#090909" offset=".37"></stop>
                            <stop stop-color="#0c0b09" offset=".53"></stop>
                            <stop stop-color="#16110c" offset=".66"></stop>
                            <stop stop-color="#261c11" offset=".78"></stop>
                            <stop stop-color="#3e2b18" offset=".89"></stop>
                            <stop stop-color="#5c3f21" offset=".99"></stop>
                            <stop stop-color="#604122" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse" y2="443.62" x2="81.11" y1="411.38" x1="79.7"
                            id="bi">
                            <stop stop-opacity=".5" stop-color="#fff" offset="0"></stop>
                            <stop stop-opacity=".38" stop-color="#fefefe" offset=".07"></stop>
                            <stop stop-opacity=".27" stop-color="#fefefe" offset=".17"></stop>
                            <stop stop-opacity=".17" stop-color="#fdfdfd" offset=".27"></stop>
                            <stop stop-opacity=".09" stop-color="#fdfdfd" offset=".38"></stop>
                            <stop stop-opacity=".04" stop-color="#fdfdfd" offset=".52"></stop>
                            <stop stop-opacity="0" stop-color="#fdfdfd" offset=".68"></stop>
                            <stop stop-opacity="0" stop-color="#fdfdfd" offset="1"></stop>
                        </linearGradient>
                        <linearGradient gradientUnits="userSpaceOnUse"
                            gradientTransform="translate(-8.09 4.61) rotate(-2.79)" y2="170.77" x2="91.3" y1="162.16"
                            x1="94.5" id="bj">
                            <stop stop-color="#3f3f3f" offset=".13"></stop>
                            <stop stop-color="#090909" offset="1"></stop>
                        </linearGradient>
                        <linearGradient xlink:href="#bj"
                            gradientTransform="translate(31.76 -5.44) rotate(5.03) scale(.81 1)" y2="171.47" x2="86.96"
                            y1="164.53" x1="89.54" id="bk"></linearGradient>
                        <linearGradient xlink:href="#bj"
                            gradientTransform="translate(-16.29 4.64) rotate(-2.49) scale(1.1 1)" y2="172.9" x2="94.31"
                            y1="166.02" x1="96.87" id="bl"></linearGradient>
                    </defs>
                    <g id="c">
                        <g>
                            <path style="fill:url(#d);"
                                d="M103.28,444.18l8.98,31.12s-3.3,10.51-20.97,10.46c-17.68-.05-21.18-10.93-21.18-10.93l9.38-29.9,23.8-.74Z">
                            </path>
                            <path style="fill:url(#e);"
                                d="M91.32,474.9c16.6,0,20.97-4.48,20.97-4.48v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,4.48,20.64,4.48Z">
                            </path>
                            <path style="fill:url(#f);"
                                d="M70.84,127.87s-.02-17.57,0-20.15,14.01-38.61,16.18-44.26,6.7-5.39,8.75,0,16.68,41.99,16.68,43.93-.04,330.65,0,333.41,8.09,17.79,8.09,19.32,1.69,20.06-29.06,20.06-29.06-18.5-29.06-20.06,8.13-16.64,8.26-19.32,.17-312.94,.17-312.94Z">
                            </path>
                            <g>
                                <path style="fill:url(#g);"
                                    d="M91.48,119.36c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#h);"
                                    d="M91.48,146.44c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#i);"
                                    d="M91.48,183.1c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#j);"
                                    d="M91.48,258.41c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#k);"
                                    d="M91.48,333.63c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#l);"
                                    d="M91.48,391.26c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#m);"
                                    d="M91.48,411.57c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#n);"
                                    d="M91.48,419.17c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#o);"
                                    d="M91.48,428.25c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#p);"
                                    d="M91.48,440.06c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#q);"
                                    d="M91.48,450.71c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                            </g>
                            <path style="fill:url(#r);"
                                d="M70.84,127.87s-.02-17.57,0-20.15c.01-1.29,2.64,11.56,20.81,11.56s20.81-12.86,20.81-11.89c0,1.94-.04,330.65,0,333.41,.02,1.36-3.06,13.05-20.97,13.05s-20.87-11.69-20.81-13.05c.12-2.68,.17-312.94,.17-312.94Z">
                            </path>
                            <path style="fill:url(#s);"
                                d="M91.48,480.19c32.61,0,29.06-20.06,29.06-20.06l-8.1-18.08s-2.39,11.81-20.97,11.81-20.81-13.05-20.81-13.05l-8.26,19.32s-3.55,20.06,29.06,20.06Z">
                            </path>
                            <path style="fill:url(#t);"
                                d="M70.84,107.72s4.18,11.56,20.81,11.56,20.81-11.89,20.81-11.89c0,0-14.86-40.33-17.12-44.93s-6.38-3.6-7.87,0-16.63,45.27-16.63,45.27Z">
                            </path>
                        </g>
                        <g id="u">
                            <path style="fill:url(#v);"
                                d="M131.16,17c2.99-4.19,17-17,18-17s12.08,8.58,17.78,17c14.22,21,27.22,38,27.22,62V411c0,33-92,31-91,0s-1-299,0-329,23-58,28-65Z">
                            </path>
                            <path style="fill:url(#w); opacity:.22;"
                                d="M131.05,17c2.99-4.19,17-17,18-17s12.08,8.58,17.78,17c14.22,21,27.22,38,27.22,62V411c0,33-92,31-91,0s-1-299,0-329,23-58,28-65Z">
                            </path>
                            <path style="fill:url(#x);"
                                d="M102.66,117.5s6,24,45.5,24,46-24,46-24l-.5,74s-12.5,15-45,15-46-15-46-15V117.5Z">
                            </path>
                            <path style="fill:url(#y);"
                                d="M149.05,0s9.4,5.19,17.78,17,27.22,35.73,27.22,62,.11,38.5,.11,38.5c0,0-1.15,6.16-9.28,12.79-6.39,5.2-16.71,11.21-36.72,11.21-45.5,0,.89-141.5,.89-141.5Z">
                            </path>
                            <path style="fill:url(#z);"
                                d="M147.97,1.5s-9.4,5.19-17.78,17c-8.38,11.81-27.22,35.73-27.22,62,0,26.27-.11,38.5-.11,38.5,0,0,1.84,8.63,15.38,15.74,6.82,3.58,15.4,6.75,30.63,6.75,45.5,0-.9-140-.9-140Z">
                            </path>
                            <path style="fill:url(#aa);"
                                d="M105.66,209.83l-2.46,202.5s.23,32.96,46.76,33.93c43.35,.91,46.66-21.97,46.66-21.97l.11-231.42-91.07,16.96Z">
                            </path>
                            <path style="fill:url(#ab);"
                                d="M193.63,138.78s-2.38,275.86-2.31,278.63c.03,1.35-9.7,16.7-33.71,16.62s-33.8-23.73-33.71-25.09c.14-2.12-14.71-164.59,11.97-234.48,7.01-18.36,41.9,7.85,57.76-35.68Z">
                            </path>
                        </g>
                        <g>
                            <path style="fill:url(#ac);"
                                d="M212.86,500.89l8.98,31.12s-3.3,10.51-20.97,10.46c-17.68-.05-21.18-10.93-21.18-10.93l9.38-29.9,23.8-.74Z">
                            </path>
                            <path style="fill:url(#ad);"
                                d="M200.9,531.62c16.6,0,20.97-4.48,20.97-4.48v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,4.48,20.64,4.48Z">
                            </path>
                            <path style="fill:url(#ae);"
                                d="M180.42,184.58s-.02-17.57,0-20.15,14.01-38.61,16.18-44.26,6.7-5.39,8.75,0c2.05,5.39,16.68,41.99,16.68,43.93s-.04,330.65,0,333.41,8.09,17.79,8.09,19.32,1.69,20.06-29.06,20.06-29.06-18.5-29.06-20.06,8.13-16.64,8.26-19.32,.17-312.94,.17-312.94Z">
                            </path>
                            <g>
                                <path style="fill:url(#af);"
                                    d="M201.06,468.29c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ag);"
                                    d="M201.06,475.89c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ah);"
                                    d="M201.06,484.97c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ai);"
                                    d="M201.06,496.78c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#aj);"
                                    d="M201.07,390.34c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ak);"
                                    d="M201.07,447.98c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#al);"
                                    d="M201.07,315.12c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#am);"
                                    d="M201.07,239.82c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#an);"
                                    d="M201.07,203.16c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ao);"
                                    d="M201.07,176.08c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                                <path style="fill:url(#ap);"
                                    d="M201.06,507.47c16.6,0,20.97-11.97,20.97-11.97v3.39s-3.88,11.81-20.97,11.81-20.64-11.23-20.64-11.23v-3.96s4.05,11.97,20.64,11.97Z">
                                </path>
                            </g>
                            <path style="fill:url(#aq);"
                                d="M180.42,184.58s-.02-17.57,0-20.15c.01-1.29,2.64,11.56,20.81,11.56s20.81-12.86,20.81-11.89c0,1.94-.04,330.65,0,333.41,.02,1.36-3.06,13.05-20.97,13.05s-20.87-11.69-20.81-13.05c.12-2.68,.17-312.94,.17-312.94Z">
                            </path>
                            <path style="fill:url(#ar);"
                                d="M201.06,536.9c32.61,0,29.06-20.06,29.06-20.06l-8.1-18.08s-2.39,11.81-20.97,11.81-20.81-13.05-20.81-13.05l-8.26,19.32s-3.55,20.06,29.06,20.06Z">
                            </path>
                            <path style="fill:url(#as);"
                                d="M180.42,164.44s4.18,11.56,20.81,11.56,20.81-11.89,20.81-11.89c0,0-14.86-40.33-17.12-44.93s-6.38-3.6-7.87,0-16.63,45.27-16.63,45.27Z">
                            </path>
                        </g>
                        <g>
                            <path style="fill:url(#at);"
                                d="M32.69,439.07c-.14,2.86-2.97,10.79,15.3,12.66,17.94,1.43,19.6-8.7,19.6-8.7,0,0,3.28-21.39,3.85-27.96,.39-4.44-.33-10.13-1.07-16.32-.71-2.13-18.48-13.36-19.51-13.29-7.93,.55-10.9,6.5-12.66,10.9-1.45,3.62-5.38,39.85-5.51,42.72Z">
                            </path>
                            <path style="fill:url(#au);"
                                d="M106.84,454.41l3.26,19.01s-2.53,3.14-13.76,1.82-12.99-5.17-12.99-5.17l8.31-16.95,15.18,1.3Z">
                            </path>
                            <path style="fill:url(#av);"
                                d="M76.16,454.7l-1.28,20.7s-3.42,2.66-14.85-1.5-12.31-8.62-12.31-8.62l12.88-15.68,15.56,5.11Z">
                            </path>
                            <path style="fill:url(#aw);"
                                d="M81.49,464.75l.92,10.27s-1.48,1.56-7.41,.37c-5.93-1.19-6.7-3.32-6.7-3.32l5.16-8.67,8.03,1.35Z">
                            </path>
                            <path
                                style="fill:url(#ax); filter:url(#ay); stroke:#2b2b2b; stroke-miterlimit:10; stroke-width:2px;"
                                d="M185.62,458.89l-94.24-40.73L18.5,351.66l-1.1-9.25,5.94-20.7,39.85-24,17.39-81.03s3.55-39.08,22.06-40.18,23.1,49.89,23.1,49.89l14.51,118.44,40.51,77.5,4.84,24.44v12.11Z">
                            </path>
                            <line style="fill:none; stroke:#2b2b2b; stroke-miterlimit:10; stroke-width:.75px;"
                                y2="426.74" x2="126.39" y1="448.76" x1="183.2"></line>
                            <line style="fill:none; stroke:#2b2b2b; stroke-miterlimit:10; stroke-width:.75px;"
                                y2="342.41" x2="17.4" y1="368.13" x1="51.09"></line><text
                                style="fill:#333; font-size:12.27px;"
                                transform="translate(27.32 341.67) rotate(34.86) scale(1.05 .83) skewX(27.74)">
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700; letter-spacing:0em;"
                                    y="0" x="0">U</tspan>
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700;"
                                    y="0" x="7.92">I</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="11.3">V</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="18.56">E</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:-.02em;"
                                    y="0" x="25.89">R</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="33.65">S</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="41.08">E</tspan>
                            </text>
                            <path style="fill:url(#ba);"
                                d="M123.31,450.69s-12.22,5.01-20.31,5.45-27.69-1.87-36.99-3.63-18.17-7.27-18.17-7.27l-1.49-30.72,2.31-34.68s6.39-114.92,6.88-121.36,.41-18.29,1.21-25.12,8.31-31.4,9.06-32.72,3.66-3.44,3.66-3.44l9.08-22.79s1.32-1.93,7.27-9.41c3.07-3.87,8.37-10.11,13.21-10.4,4.52-.27,8.82,5.09,10.4,7.6,2.52,4,3.28,5.35,4.36,7.99s11.06,32.96,11.99,43.03,4.46,120.55,4.46,120.55l-6.94,116.92Z">
                            </path>
                            <path style="fill:url(#bb);"
                                d="M123.31,450.69s-12.22,5.01-20.31,5.45c-8.09,.44-27.69-1.87-36.99-3.63s-18.17-7.27-18.17-7.27l-1.49-30.72,2.31-34.68s6.39-114.92,6.88-121.36,.41-18.29,1.21-25.12,8.31-31.4,9.06-32.72,3.66-3.44,3.66-3.44l9.08-22.79s1.32-1.93,7.27-9.41c3.07-3.87,8.37-10.11,13.21-10.4,4.52-.27,8.82,5.09,10.4,7.6,2.52,4,3.28,5.35,4.36,7.99s11.06,32.96,11.99,43.03,4.46,120.55,4.46,120.55l-6.94,116.92Z">
                            </path>
                            <polygon style="fill:url(#bc); stroke:#4c4c4c; stroke-miterlimit:10;"
                                points="56.81 407.75 49.88 415.51 1.82 501.05 .5 504.69 .5 524.34 51.53 472.32 58.96 448.54 61.77 408.41 56.81 407.75">
                            </polygon>
                            <polygon style="fill:url(#bd); stroke:#5b5b5b; stroke-miterlimit:10;"
                                points="48.05 475.95 51.28 458.33 52.01 454.37 51.64 456.37 14.76 500.65 9.73 506.09 8.49 516.23 9.9 515.12 48.05 475.95">
                            </polygon>
                            <path style="fill:url(#be);"
                                d="M78.06,232.21c1.17,1.37-.73,7.04,.83,8.64,3.18,3.27,9.84,2.28,9.98,1.98,3.28-6.94,1.14-17.53-1.04-22.1s-7.68-9.41-7.95-9.55-3.12-3.98-3.3-4.07c-.5-.25-3.22,.25-3.22,.25,0,0-7.14,3.92-8.02,4.71-4.79,4.29-6.89,12.07-6.32,11.13,.51-.84,6.87-3.31,10.71-2.96,.76,.07,6.91,10.31,8.34,11.97Z">
                            </path>
                            <path style="fill:none; stroke:#777574; stroke-miterlimit:10; stroke-width:.75px;"
                                d="M78.27,248.23c.82-.21,3.44,1.21,6.65,1.57,4.12,.46,9.01-.06,9.99,.21,3.56,.95,3.3,8.34-.91,10.07-.87,.36-5.26,1-9.29,.7-3.3-.25-6.42-1.71-7.27-2.39-2.57-2.09-1.76-9.49,.83-10.16Z">
                            </path><text
                                style="fill:#666; font-family:Bahnschrift, Bahnschrift; font-size:5.69px; font-variation-settings:'wght' 400, 'wdth' 100;"
                                transform="translate(121.16 383.28) rotate(28.29) scale(1.26 .89) skewX(19.94)">
                                <tspan y="0" x="0">@ken</tspan>
                                <tspan style="letter-spacing:-.01em;" y="0" x="14.3">n</tspan>
                                <tspan style="letter-spacing:0em;" y="0" x="17.4">y</tspan>
                                <tspan style="letter-spacing:0em;" y="0" x="20.09">otsu</tspan>
                            </text><text style="fill:#333; font-size:12.31px;"
                                transform="translate(120.21 399.04) rotate(28.29) scale(.98 .89) skewX(24.96)">
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700; letter-spacing:0em;"
                                    y="0" x="0">U</tspan>
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700;"
                                    y="0" x="7.95">I</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="11.33">V</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="18.62">E</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:-.02em;"
                                    y="0" x="25.98">R</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="33.76">S</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="41.22">E</tspan>
                            </text>
                            <polygon style="fill:url(#bf); opacity:.82;"
                                points="122.02 377.86 133.5 384.43 133.77 377.45 122.02 370.93 122.02 377.86"></polygon>
                            <path style="fill:url(#bg);"
                                d="M61.77,455.26c-.14,2.86-2.97,10.79,15.3,12.66,17.94,1.43,19.6-8.7,19.6-8.7,0,0,3.28-21.39,3.85-27.96,.39-4.44-.33-10.13-1.07-16.32-.71-2.13-3.14-3.64-4.16-3.45-3.11,.57-17.2,2.93-20.86,4.28s-9.03,9.77-9.03,9.77c0,0-3.5,26.86-3.63,29.72Z">
                            </path>
                            <path style="fill:url(#bh);"
                                d="M99.03,154.6c15.8,0,23.06,42.39,25.97,53.78,.59,3.25,2.05,16.9,2.05,16.9,0,0-16.95-10.91-19.38-28.19s-12.66-23.72-18.17-24.39-13.29,7.56-13.29,7.56c0,0,.61-2.93,2.33-5.85,4.4-7.46,13.63-19.82,20.48-19.82Z">
                            </path><text style="fill:#333; font-size:12.31px;"
                                transform="translate(112.43 320.11) rotate(-88.85) scale(.98) skewX(13.92)">
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700; letter-spacing:0em;"
                                    y="0" x="0">U</tspan>
                                <tspan
                                    style="font-family:Bahnschrift-Bold, Bahnschrift; font-variation-settings:'wght' 700, 'wdth' 100; font-weight:700;"
                                    y="0" x="7.95">I</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="11.33">V</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="18.62">E</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:-.02em;"
                                    y="0" x="25.98">R</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100; letter-spacing:0em;"
                                    y="0" x="33.76">S</tspan>
                                <tspan
                                    style="font-family:Bahnschrift, Bahnschrift; font-variation-settings:'wght' 400, 'wdth' 100;"
                                    y="0" x="41.22">E</tspan>
                            </text>
                            <path style="fill:url(#bi);"
                                d="M61.77,455.26c-.14,2.86-2.97,10.79,15.3,12.66,17.94,1.43,19.6-8.7,19.6-8.7,0,0,3.28-21.39,3.85-27.96,.39-4.44-.33-10.13-1.07-16.32-.71-2.13-3.14-3.64-4.16-3.45-3.11,.57-17.2,2.93-20.86,4.28s-9.03,9.77-9.03,9.77c0,0-3.5,26.86-3.63,29.72Z">
                            </path>
                            <line style="fill:#fff; stroke:#2b2b2b; stroke-miterlimit:10; stroke-width:.5px;"
                                y2="426.74" x2="126.39" y1="432.01" x1="124.42"></line>
                            <path style="fill:url(#bj);"
                                d="M91.12,170.73c2.47,.54,5.71-8.54,3.35-8.72s-5.82,8.18-3.35,8.72Z"></path>
                            <path style="fill:url(#bk);"
                                d="M86.98,171.58c1.41,.66,4.25-6.41,2.87-6.77s-4.28,6.1-2.87,6.77Z"></path>
                            <path style="fill:url(#bl);"
                                d="M94.44,172.88c2.08,.44,4.88-6.87,2.88-7.01s-4.96,6.57-2.88,7.01Z"></path>
                        </g>
                    </g>
                </svg>
            </div>
        </div>
        <el-table :data="tableData" style="width: 50%">
            <el-table-column prop="id" label="ID" width="220" />
            <el-table-column prop="filename" label="文件名" width="220" />
            <el-table-column prop="prediction" label="预测结果" width="400" />
            <el-table-column label="更新" width="100">
                <template #default="{ row }">
                    <el-button @click="updateRow(row)">Update</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="danger" @click="clearData" style="margin-top: 20px;">清除所有结果</el-button>
    </div>
    <button id="button">
                <p id="to-launch">BEST模型验证结果</p>
    </button>
    <div style="display: flex;justify-content: space-between;align-items: center;">
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxF1分数曲线</h3>
        <img :src="'/val/BoxF1_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxPression曲线</h3>
        <img :src="'/val/BoxP_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxPR曲线</h3>
        <img :src="'/val/BoxPR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>BoxRecall曲线</h3>
        <img :src="'/val/BoxR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskF1分数曲线</h3>
        <img :src="'/val/MaskF1_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskPression曲线</h3>
        <img :src="'/val/MaskP_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskR曲线</h3>
        <img :src="'/val/MaskR_curve.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>MaskPR曲线</h3>
        <img :src="'/val/MaskPR_curve.png'" alt="6666" />
        </div>
    </div>
    <div style="display: flex;justify-content: space-between;align-items: center;">
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val_batch_labels</h3>
        <img :src="'/val/val_batch0_labels.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val_batch_pred</h3>
        <img :src="'/val/val_batch0_pred.jpg'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val混淆矩阵</h3>
        <img :src="'/val/confusion_matrix.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val混淆矩阵_归一化</h3>
        <img :src="'/val/confusion_matrix_normalized.png'" alt="6666" />
        </div>
        <div v-if="result" style="display: flex;flex-direction: column;margin-left: 20px;width: 150px;">
        <h3>val_acc</h3>
        <p>0.72</p>
        </div>
    </div>
    <div v-if="result" style="margin-top: 40px;">
        <img src="/val.png" alt="">
    </div>
    <div>
        <p>
            混淆矩阵:
            一个表格，用于描述分类模型的性能。它显示了真实标签与模型预测标签之间的关系。对于二分类问题，结构如下：
        </p>
        <img src="/t.png" alt="">
        <img src="/k.png" alt="">
        <p>
            IoU是用于评估目标检测模型性能的指标,特别是在分割任务中。它表示预测区域与真实区域的交集与并集的比值。
        </p>
        <p>FPS是衡量模型处理速度的指标,表示每秒钟处理的帧数。在视频处理和实时应用中,FPS是一个重要的性能指标。</p>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount } from 'vue';
import DPlayer from 'dplayer';
const dplayerRef1 = ref(null);
import axios from 'axios';

const tableData = ref([]); // 使用 ref 来创建响应式数据
const dplayerRef2 = ref(null);
let player = null; // DPlayer 实例
let playe = null;

const tableRowClassName = ({ row, rowIndex }) => {
    if (rowIndex === 0) {
        return 'header-row'; // 表头行
    } else if (rowIndex === 1) {
        return 'first-row'; // 第一行
    } else if (row.score > 90) {
        return 'high-score-row'; // 高分行
    } else if (row.score < 60) {
        return 'low-score-row'; // 低分行
    }
    return 'default-row'; // 默认样式
};


const onFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
        const url = URL.createObjectURL(file); // 创建本地视频文件的 URL
        initializePlayer(url); // 初始化播放器
    }
};

const initializePlayer = (url) => {
    // 销毁旧的播放器实例（如果存在）
    if (player) {
        player.destroy();
    }

    // 创建新的 DPlayer 实例
    player = new DPlayer({
        container: dplayerRef1.value,
        video: {
            url: url,
            type: 'auto', // 自动检测视频类型
        },
    });
};

// 在组件销毁时销毁播放器实例
onBeforeUnmount(() => {
    if (player) {
        player.destroy();
    }
});

const onFileChange1 = (event) => {
    const file = event.target.files[0];
    if (file) {
        const url = URL.createObjectURL(file); // 创建本地视频文件的 URL
        initializePlayer1(url); // 初始化播放器
    }
};

const initializePlayer1 = (url) => {
    // 创建新的 DPlayer 实例
    playe = new DPlayer({
        container: dplayerRef2.value,
        video: {
            url: url,
            type: 'auto', // 自动检测视频类型
        },
    });
};

// 在组件销毁时销毁播放器实例
onBeforeUnmount(() => {
    if (playe) {
        playe.destroy();
    }
});

// 获取数据的函数
const fetchData = async () => {
    try {
        const response = await axios.get('http://localhost:5000/prediction_results'); // 替换为您的后端API
        tableData.value = response.data; // 假设返回的数据是数组
    } catch (error) {
        console.error('获取数据失败:', error);
    }
};
// 组件挂载时获取数据
onMounted(() => {
    fetchData();
});

// 在组件挂载时获取数据
fetchData();

// 重置数据
const resetData = async () => {
    await fetchData(); // 重新获取数据
};

// onMounted(() => {
//   const dp = new DPlayer({
//     container: dplayerRef2.value,
//     screenshot: true,
//     video: {
//       url: '/POSE0.mp4', // 替换为你的视频文件路径

//     }
//   });
// });
// 清空数据的函数
const clearData = async () => {
    try {
        await axios.delete('http://localhost:5000/clear_data'); // 调用清空数据的路由
        fetchData(); // 清空后重新获取数据
        alert('所有数据已清空');
    } catch (error) {
        console.error('清空数据失败:', error);
        alert('清空数据失败');
    }
};
const selectedFile = ref(null);
const result = ref(null);

const onFileChangeI = (event) => {
    selectedFile.value = event.target.files[0]; // 获取选择的文件
    fileName.value = selectedFile.value ? selectedFile.value.name : null; // 更新文件名
};
const updateRow = async (row) => {
    // 实现更新逻辑
    console.log('更新行:', row);

    // 假设我们要更新 prediction 字段
    let detectionResult = '未检测到物体'; // 默认值

    // 这里是您检测的逻辑
    // 如果检测到肺结核
    if (true) {
        detectionResult = '检测到肺结核，位置在 xxx'; // 替换 'xxx' 为实际位置
    }

    // 更新行的 prediction
    row.prediction = detectionResult;

    // 如果需要将更新的数据发送到服务器
    try {
        const response = await axios.put(`http://127.0.0.1:5000/update/${row.id}`, {
            prediction: row.prediction,
        });
        console.log('更新成功:', response.data);
    } catch (error) {
        console.error('更新失败:', error);
    }
};

// 计算属性，用于格式化 prediction
const formattedPrediction = computed(() => {
    // 这里可以对 prediction 进行处理和格式化
    return result.value.prediction
        .replace(/(boxes.*?)(\n)/, '<span class="bold">$1</span>$2') // 加粗 boxes 行
        .replace(/(masks.*?)(\n)/, '<span class="bold">$1</span>$2') // 加粗 masks 行
        .replace(/(names.*?)(\n)/, '<span class="bold">$1</span>$2') // 加粗 names 行
        .replace(/(orig_shape.*?)(\n)/, '<span class="bold">$1</span>$2') // 加粗 orig_shape 行
        .replace(/(path.*?)(\n)/, '<span class="bold">$1</span>$2'); // 加粗 path 行
});

// 上传图片的函数
const uploadImage = async () => {
    const formData = new FormData();
    formData.append('file', selectedFile.value);

    try {
        const response = await axios.post('http://127.0.0.1:5000/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        result.value = response.data;
        console.log(result.value);
        const predictionStr = JSON.stringify(result.value.prediction);

        // 找到 keypoints 的位置
        const keypointsIndex = predictionStr.indexOf('"keypoints:"');

        // 找到第一个 } 的位置
        const firstBraceIndex = predictionStr.indexOf('}', keypointsIndex);

        // 截取从 keypoints 开始到第一个 } 的部分
        const inputString = predictionStr.substring(keypointsIndex, firstBraceIndex + 1);
        // 找到 masks 和 names 的起始位置
        const masksStart = inputString.indexOf('masks:');
        const namesStart = inputString.indexOf('names:');

        // 检查是否找到 masks 和 names
        if (masksStart === -1 || namesStart === -1) {
            console.log('没有找到 masks 或 names 的内容');
        } else {
            // 截取 masks 部分
            const masksEnd = inputString.indexOf('\n', masksStart); // 找到 masks 结束的位置
            const masks = inputString.substring(masksStart, masksEnd).trim(); // 提取并去除多余空格

            // 截取 names 部分
            const namesEnd = inputString.indexOf('}', namesStart) + 1; // 找到 names 结束的位置
            const names = inputString.substring(namesStart, namesEnd).trim(); // 提取并去除多余空格

            // 默认值
            let detectionResult = '未检测到肺结核';

            // 检查 names 中是否包含 'yes'
            if (names.includes("'yes'")) {
                detectionResult = '检测到肺结核，位置在 xxx'; // 替换 'xxx' 为实际位置
            }
            // 调用 updateRow 方法并传递 detectionResult
            updateRow(row, detectionResult);

        }
        // 在上传成功后调用 fetchData 更新表格数据
        await fetchData(); // 确保 fetchData 是一个异步函数并等待其完成
    } catch (error) {
        console.error('上传失败:', error);
    }
};
const fileInput = ref(null); // 创建一个ref来引用文件输入
const fileName = ref(null); // 创建一个ref来存储文件名
const triggerFileInput = () => {
    fileInput.value.click(); // 触发文件输入
};
// 定义响应式变量
const trainingMessage = ref('');
const errorMessage = ref('');

// 定义训练模型的方法
const trainModel = async () => {
    trainingMessage.value = '正在训练模型...';
    errorMessage.value = '';

    try {
        const response = await axios.post('http://localhost:5000/train_model');
        trainingMessage.value = response.data.message; // 显示训练完成的消息
    } catch (error) {
        errorMessage.value = error.response ? error.response.data.error : '训练过程中发生错误';
    }
};
</script>
<style lang="scss" scoped>
.result-container {
    text-align: left;
    /* 居左对齐 */
    margin: 20px;
    /* 外边距 */
}

.result-text {
    white-space: pre-wrap;
    /* 保留换行和空格 */
    word-wrap: break-word;
    /* 自动换行 */
    background-color: #e0ebf0;
    /* 背景色 */
    border: 1px solid #ddd;
    /* 边框 */
    padding: 10px;
    /* 内边距 */
    border-radius: 5px;
    /* 圆角 */
    font-family: monospace;
    /* 使用等宽字体 */
    color: #3b4144;
    /* 字体颜色 */
}

.bold {
    font-weight: bold !important;
    /* 强制加粗 */
    /* 加粗 */
}

.even-row {
    background-color: #f9f9f9;
    /* 偶数行背景色 */
}

.odd-row {
    background-color: #ffffff;
    /* 奇数行背景色 */
}

.high-score-row {
    background-color: #d4edda;
    /* 高分行背景色 */
}

.low-score-row {
    background-color: #f8d7da;
    /* 低分行背景色 */
}

.header-row {
    font-weight: bold;
    /* 表头行样式 */
}

.container {
    position: relative;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    width: 200px;
    height: 200px;
    color: rgb(131, 131, 131);
    -webkit-transition: 400ms ease-in-out;
    transition: 400ms ease-in-out;
    transform: translateY(20px);
}

/* clickHandler, as the name suggests, handles clicks. 
       Its a label for invisible checkbox, positioned absolutely and taking whole container space.
       Comes with insane z-index. Stays above .container
       When clicked, will change input state to checked */
#shuttle-wrapper {
    position: absolute;
    opacity: 0;
    -webkit-transition: 200ms ease-in;
    transition: 200ms ease-in;
    z-index: 200;
}

#b {
    width: 80px;
    height: auto;
    z-index: 200;
}

#shadow {
    position: absolute;
    width: 100px;
    height: 80px;
    -webkit-transform: rotateX(45deg) rotateZ(45deg);
    transform: rotateX(45deg) rotateZ(45deg);
    bottom: -10px;
    z-index: -1;
    background: radial-gradient(rgba(0, 0, 0, 0.382) 0%, rgba(0, 0, 0, 0) 70%);
}

#clickHandler {
    display: block;
    position: absolute;
    inset: 0;
    z-index: 999;
}

#clickHandler:hover {
    cursor: pointer;
}

.container:hover #shuttle-wrapper {
    opacity: 1;
    -webkit-transform: translateY(-100px) translateZ(140px);
    -ms-transform: translateY(-100px) translateZ(140px);
    transform: translateY(-100px) translateZ(140px);
    -webkit-transition: 400ms ease-in-out;
    transition: 400ms ease-in-out;
}

#checkbox {
    /* comment out to actually see the checkbox state */
    display: none;
    /*  */
    position: absolute;
    bottom: 0px;
    width: 200px;
}

/* this pseudo-selectors will fire animations when 
       clickHandler changes checkbox state to checked */
#checkbox:checked~#shuttle-wrapper #b {
    -webkit-animation-name: launch_31;
    animation-name: launch_31;
    -webkit-animation-timing-function: ease-in-out;
    animation-timing-function: ease-in-out;
    -webkit-animation-duration: 6s;
    animation-duration: 6s;
}

#checkbox:checked~#shuttle-wrapper #shadow {
    -webkit-animation: shadow-launch 6s ease-in-out;
    animation: shadow-launch 6s ease-in-out;
}

#checkbox:checked~#to-hover {
    -webkit-animation-name: fadeout_010;
    animation-name: fadeout_010;
    -webkit-animation-duration: 6s;
    animation-duration: 6s;
}

#checkbox:checked~#button #to-launch {
    -webkit-animation-name: fadeout_010;
    animation-name: fadeout_010;
    -webkit-animation-duration: 6s;
    animation-duration: 6s;
}

/* and this will take care of hover */
#clickHandler:hover~#button {
    width: 200px;
    height: 200px;
    -webkit-transform: translate3d(0px, -16px, 0px) rotateX(51deg) rotateZ(43deg);
    transform: translate3d(0px, -16px, 0px) rotateX(51deg) rotateZ(43deg);
    -webkit-box-shadow: 2px 2px 0 2px #b2b2b2, -1px 0 28px 0 rgba(255, 250, 225, 0.512),
        28px 28px 28px 0 rgba(28, 27, 88, 0.315);
    box-shadow: 2px 2px 0 2px #b2b2b2, -1px 0 28px 0 rgba(255, 250, 225, 0.512),
        28px 28px 28px 0 rgba(28, 27, 88, 0.315);
    -webkit-transition-delay: 0ms;
    transition-delay: 0ms;
}

.container:hover {
    -webkit-transform: translateY(30%);
    -ms-transform: translateY(30%);
    transform: translateY(30%);
}

#to-launch {
    color: rgb(127, 164, 240);
    font-size: large;
    font-weight: bold;
    -webkit-transition: ease-out 200ms;
    transition: ease-out 200ms;
}

#to-hover {
    margin-top: 1em;
    text-align: center;
    -webkit-transition: ease-out 200ms;
    transition: ease-out 200ms;
}

#tag {
    opacity: 0;
    color: rgb(43, 43, 43);
    position: absolute;
}

#platform {
    position: absolute;
    width: 70%;
    height: 0;
    border-radius: 16px;
    background-color: rgb(234, 234, 234);
    -webkit-transition: 0.3s ease-in-out;
    transition: 0.3s ease-in-out;
    -webkit-transition-delay: 150ms;
    transition-delay: 150ms;
    border: solid 0px rgba(42, 42, 42, 0);
    outline: dashed rgba(255, 173, 57, 0);
    -moz-outline-radius: 16px;
    outline-width: 0px;
    outline-offset: 0px;
}

#clickHandler:hover~#button #platform {
    height: 70%;
    background: rgb(244, 244, 244);
    border: solid 4px rgb(42, 42, 42);
    outline: dashed rgb(255, 172, 57);
    outline-width: 4px;
    outline-offset: -4px;
    -webkit-animation: shake_010 80ms infinite;
    animation: shake_010 80ms infinite;
}

.caution {
    -webkit-transform: translateY(-200);
    -ms-transform: translateY(-200);
    transform: translateY(-200);
    position: absolute;
    width: 100%;
    text-align: center;
}

#checkbox:checked~.caution {
    -webkit-animation-name: fadeout_010;
    animation-name: fadeout_010;
    -webkit-animation-duration: 6s;
    animation-duration: 6s;
}

#caution-left {
    -webkit-transition: 200ms ease;
    transition: 200ms ease;
    opacity: 0;
    -webkit-transform: translate3d(83px, 0px, 0px) rotateX(0deg) rotateY(0deg) rotateZ(-90deg);
    transform: translate3d(83px, 0px, 0px) rotateX(0deg) rotateY(0deg) rotateZ(-90deg);
}

#caution-right {
    -webkit-transition: 200ms ease;
    transition: 200ms ease;
    opacity: 0;
    -webkit-transform: translate3d(0px, 62px, 0px) rotateX(0deg) rotateY(0deg) rotateZ(0deg);
    transform: translate3d(0px, 62px, 0px) rotateX(0deg) rotateY(0deg) rotateZ(0deg);
}

#clickHandler:hover~#button .caution #caution-left {
    opacity: 1;
    -webkit-transition: 400ms ease-in;
    transition: 400ms ease-in;
    animation: glow_010 1.2s infinite;
}

#clickHandler:hover~#button .caution #caution-right {
    opacity: 1;
    -webkit-transition: 400ms ease-in;
    transition: 400ms ease-in;
    animation: glow_010 1.2s infinite;
}

#button {
    width: 200px;
    height: 80px;
    border: none;
    border-radius: 1.8rem;
    -webkit-transition: 0.4s ease-in-out;
    transition: 0.4s ease-in-out;
    -webkit-box-shadow: 5px 5px 8px 0 rgba(28, 27, 88, 0.315);
    box-shadow: 5px 5px 8px 0 rgba(28, 27, 88, 0.315);
    background: -webkit-gradient(linear, left top, right top, from(#ffffff), to(#ececec));
    background: linear-gradient(125deg, #ffffff 50%, #f5f5ff);
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    font-family: inherit;
    -webkit-transition-delay: 100ms;
    transition-delay: 100ms;
}

#clickHandler:hover~#button #to-launch {
    opacity: 0;
}

#clickHandler:hover~#to-hover {
    opacity: 0;
}

#checkbox:checked~#button #tag {
    -webkit-animation: tag-transition 5s ease-in-out;
    animation: tag-transition 5s ease-in-out;
}

/* launch sequence, takes only 20% of duration to completely fade out,
       and stays there til 90% */

@keyframes launch_31 {
    5% {
        opacity: 1;
    }

    15%,
    90% {
        -webkit-transform: translateY(-200px);
        transform: translateY(-200px);
        -webkit-transition-timing-function: ease-in;
        transition-timing-function: ease-in;
        opacity: 0;
    }

    100% {
        opacity: 1;
    }
}

@keyframes shadow-launch {
    0% {
        opacity: 1;
    }

    10%,
    90% {
        opacity: 0;
    }

    100% {
        opacity: 1;
    }
}

@keyframes fadeout_010 {
    100% {
        opacity: 1;
    }

    0%,
    20%,
    90% {
        opacity: 0;
    }
}

@keyframes shake_010 {

    0%,
    50% {
        -webkit-transform: translate3d(1px, 0px, 0px);
        transform: translate3d(1px, 0px, 0px);
    }

    100% {
        -webkit-transform: translate3d(0px, 1px, 0px);
        transform: translate3d(0px, 1px, 0px);
    }
}

@keyframes glow_010 {
    0% {
        color: rgb(94, 94, 94);
        -webkit-filter: drop-shadow(0 0 0.75rem rgb(255, 214, 90));
        filter: drop-shadow(0 0 0.75rem rgb(255, 214, 90));
    }

    70% {
        color: rgba(255, 255, 255, 0);
    }

    100% {
        color: rgb(94, 94, 94);
        -webkit-filter: drop-shadow(0 0 0.75rem rgb(255, 214, 90));
        filter: drop-shadow(0 0 0.75rem rgb(255, 214, 90));
    }
}

@keyframes tag-transition {

    0%,
    100% {
        opacity: 0;
    }

    20%,
    80% {
        opacity: 1;
    }
}

.noselect {
    -webkit-touch-callout: none;
    /* iOS Safari */
    -webkit-user-select: none;
    /* Safari */
    /* Konqueror HTML */
    -moz-user-select: none;
    /* Old versions of Firefox */
    -ms-user-select: none;
    /* Internet Explorer/Edge */
    user-select: none;
}

/* From Uiverse.io by vanaparthyguptha */
.futuristic-button {
    position: relative;
    padding: 15px 60px;
    font-size: 24px;
    color: #fff;
    text-transform: uppercase;
    letter-spacing: 2px;
    background: linear-gradient(90deg, #00b4db, #0083b0);
    border: none;
    border-radius: 30px;
    cursor: pointer;
    overflow: hidden;
    box-shadow:
        0 0 20px rgba(0, 179, 219, 0.6),
        0 0 40px rgba(0, 131, 176, 0.8);
    transition:
        transform 0.4s ease,
        box-shadow 0.4s ease,
        background 0.4s ease;
}

.futuristic-button::before {
    content: "";
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle,
            rgba(255, 255, 255, 0.4),
            transparent 70%);
    transform: rotate(45deg);
    transition: transform 0.5s ease;
    z-index: -1;
}

.futuristic-button:hover::before {
    transform: translateX(50%) translateY(50%);
}

.futuristic-button::after {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border-radius: 30px;
    border: 2px solid rgba(255, 255, 255, 0.5);
    box-shadow: 0 0 30px rgba(255, 255, 255, 0.3);
    transition:
        transform 0.4s ease,
        box-shadow 0.4s ease;
    transform: scale(0.95);
    z-index: -2;
}

.futuristic-button:hover {
    transform: scale(1.05);
    background: linear-gradient(90deg, #00c6ff, #0072ff);
    box-shadow:
        0 0 30px rgba(0, 179, 219, 0.8),
        0 0 60px rgba(0, 131, 176, 1);
}

.futuristic-button:hover::after {
    transform: scale(1.1);
}

.futuristic-button:active {
    transform: scale(0.98);
    box-shadow:
        0 0 15px rgba(0, 179, 219, 0.8),
        0 0 30px rgba(0, 131, 176, 1);
}


.s {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    font-family: inherit;
    font-size: 13px;
    font-weight: 500;
    text-transform: uppercase;
    letter-spacing: 0.4px;
    color: #7e97b8;
    background-color: #e0e8ef;
    border-style: solid;
    border-width: 2px 2px 2px 2px;
    border-color: rgba(255, 255, 255, 0.333);
    border-radius: 40px 40px 40px 40px;
    padding: 16px 24px 16px 28px;
    transform: translate(0px, 0px) rotate(0deg);
    transition: 0.2s;
    box-shadow: -4px -2px 16px 0px #ffffff, 4px 2px 16px 0px rgb(95 157 231 / 48%);
}

.s:hover {
    color: #516d91;
    background-color: #E5EDF5;
    box-shadow: -2px -1px 8px 0px #ffffff, 2px 1px 8px 0px rgb(95 157 231 / 48%);
}

.s:active {
    box-shadow: none;
}

.card {
    position: relative;
    width: 11.875em;
    height: 16.5em;
    box-shadow: 0px 1px 13px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 120ms;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #fff;
    padding: 0.5em;
    padding-bottom: 3.4em;
}

.card .title {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 0.9em;
    position: absolute;
    left: 3.825em;
    bottom: 1.275em;
    font-weight: 400;
    color: #1a1b1a;
}

.card .price {
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    font-size: 0.9em;
    position: absolute;
    left: 0.625em;
    bottom: 0.625em;
    color: #000;
    text-align: center;
}

.text {
    max-width: 55px;
}

.image {
    background: rgb(241, 241, 241);
    width: 100%;
    height: 100%;
    display: grid;
    place-items: center;
}

.t input {
    display: none;
    /* 隐藏原始文件输入 */
}
</style>