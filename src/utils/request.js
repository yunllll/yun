import axios from 'axios'
const request = axios.create({
    baseURL:'http://localhost9090',
    timeout:30000
})

//加token加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config
},error =>{
    console.log('request error:'+error);
    return Promise.reject(error)
}
);

//response拦截器

request.interceptors.response.use(
    response =>{
        let res = response.data;

        //兼容
        if(typeof res ==='string'){
            res = res? JSON.parse(res):res
        }
        return res;
    },error =>{
        console.log('request error:'+error);
        return Promise.reject(error)
    }
)