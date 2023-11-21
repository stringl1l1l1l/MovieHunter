<template>
	<view class="center-container">
		<view class="login_container">
			<u-tabs :list="list" :is-scroll="false" :current="current" @click="tabChange"  lineWidth="45"
				lineColor="#000000"
				activeStyle="{
					color: '#000000',
					fontWeight: 'bold',
					transform: 'scale(1.05)'}" class="tabs">
			</u-tabs>
			
			<view v-show="isShow" class="byEmail">
				
				<u-row gutter="16" justify="center">	
					<u-col>
						<u-form :model="form" :rules="rules" ref="uForm">
							<u-form-item label="邮箱" prop="email" :required="true">
								<u-input v-model="form.email"  placeholder="请输入邮箱"  />
							</u-form-item>
						</u-form>
					</u-col>
				</u-row>
				<u-row gutter="16" justify="center">	
					<u-col>
						<u-form :model="form" :rules="rules" ref="uForm">
							<u-form-item label="密码" prop="password">
								<u-input v-model="form.password" placeholder="请输入密码" type="password"></u-input>
							</u-form-item>
						</u-form>
					</u-col>
				</u-row>
				
			 <button @click="loginBypw" type="primary" class="login-button">登录</button>
		
			 <!-- <u-button @click="login">没有账号？点我注册</u-button> -->
			<view class="tips">
				<router-link to="../Login/login" style="color: #3f9beb">没有账号？点我注册</router-link>
				<router-link to="../Login/login" style="color: #3f9beb">找回密码（TODO）</router-link>
			</view>
				
			</view>
			<view v-show="!isShow" class="byEmail">
				<u-row gutter="16" justify="center">
					<u-col span=8>
						<u-form :model="form" :rules="rules" ref="uForm">
							<u-form-item label="邮箱" prop="email">
								<u-input v-model="form.email"  placeholder="请输入邮箱"  />
							</u-form-item>
						 </u-form>
					</u-col>
					
					<u-col span=4>
						 <div v-show="!isFirst">{{ remainingTime }}s后重新获取</div>
							  <u-button
								:disabled="isCounting"
								@click="submitByEmail"
								class="code-button"
							  >
							   发送验证码
							  </u-button>
					</u-col>	
				</u-row>
				
				<u-row gutter="16" justify="center">
					<u-col>
						<u-form :model="form" :rules="rules" ref="uForm">
							<u-form-item label="验证">
							<u-input v-model="form.code" type="digit" placeholder="请输入验证码"></u-input>
							</u-form-item>
						</u-form>
					</u-col>
				</u-row>
				<button @click="loginByCode" type="primary" class="login-button">登录</button>
				
				<view class="tips">
					<router-link to="../Login/login" style="color: #3f9beb">没有账号？点我注册</router-link>
					<router-link to="../Login/findPassword" style="color: #3f9beb">找回密码</router-link>
				</view>
			</view>	
				
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isCounting: false,
				remainingTime: 60,
				isFirst:true,
				intervalId: null,
				code:'',
				 buttonText: '获取验证码',
				list: [{
						name: '密码登录'}, 
						{
						name: '验证码登录'
						}],
				current: 0,		
				isShow: true,
				form:{
					email:"",
					phone:"",
					password:""
				},
				rules:{
					email:[
						{
							required:true,
							type:"email",
							trigger: ['change','blur'],
						}
					],
						
					
				}
				
				
			};
		},
		methods:{
			submitByEmail(){
				this.$refs.uForm.validate().then(valid => {
								if (valid) {
									// console.log('验证通过');
									var email = this.form.email.replace('@','%40')
									uni.request({
										url: this.$BASE_URL.BASE_URL+'sendMsg/'+email,
										method:'GET',	
										success(res) {
											uni.showToast({
												title:"验证码已发送"
											})
											
											console.log(res)
										},
										complete(e){
											console.log(e)
										}
									})
								} else {
									console.log('验证失败');
								}
							}).catch((e) => {console.log('验证完成');
						console.log((e))});
				this.isFirst = false;
				 if (this.intervalId) {
				        clearInterval(this.intervalId); // 清除之前的计时器
				      }
					  
				this.isCounting = true;
				      this.remainingTime = 60; // 重置倒计时秒数
				      this.intervalId = setInterval(() => {
				        if (this.remainingTime > 0) {
				          this.remainingTime--;
				        } else {
				          this.isCounting = false;
				          clearInterval(this.intervalId);
				        }
				      }, 1000); // 每秒更新一次倒计时秒数
				console.log('提交')
				console.log(this.form.email)
				
				
				},

			loginBypw(){
				console.log(this.form.email)
				console.log((this.form.password))
				uni.request({
				 	url:this.$BASE_URL.BASE_URL+'loginWithPwd',
				 	method:'POST',
					dataType:'json',
				 	data:{
				 		"email":this.form.email,	
				 		"password":this.form.password
				 	},
				 	success(res) {
				 		console.log(res)
						if(res.data.code==200)
						{
							uni.setStorage({
								key:'token',
								data:res.data.data.token,
								success(data) {
									console.log('设置token成功');
									uni.showToast({
										title: '登录成功'
									});
									console.log('即将跳转');
								},
								fail() {
									uni.showToast({
										title: '设置token失败',
										icon: 'error'
									});
								}
							})
							uni.redirectTo({
								url:'/pages/recommendations/recommendations'
							})
						}
						else{
							uni.showToast({
								title:"邮箱或密码错误",
								icon: 'error'
								
							})
						}
						
				 	},
				 	fail(res) {
						uni.showToast({
							icon:'fail',
							title:"网络错误"
						})
				 		console.log(res)
				 	},
							complete() {
								console.log("complete")
							}
				 })
				
			},
			loginByCode(){
				uni.request({
				 	url:this.$BASE_URL.BASE_URL+'loginWithCode',
				 	method:'POST',
					dataType:'json',
				 	data:{
				 		"email":this.form.email,	
				 		"password":this.code
				 	},
				 	success(res) {
				 		console.log(res)
						uni.redirectTo({
							url:'/pages/recommendations/recommendations'
						})
				 	},
				 	fail(res) {
				 		console.log(res)
				 	},
							complete() {
								console.log("complete")
							}
				 })
				
			},
			login(){
				uni.redirectTo({
						url:'/pages/Login/login'
				})
			},
			handle(e){
				this.code= e
				console.log("输入结束，当前值为："+e)
			},
		tabChange(index) {
						this.current = index.index;
						this.isShow = this.current==0;
					},
		onReady() {
				this.$refs.uForm.setRules(this.rules);
			},
			 
			      handleCountDownChange({ day, hour, minute, second }) {
			             if (this.isCounting) {
			               this.buttonText = `${minute}:${second}`; // 实时更新按钮文本为倒计时剩余时间
			             }
			             if (day === 0 && hour === 0 && minute === 0 && second === 0) {
			               this.isCounting = false; // 倒计时结束
			               this.buttonText = '获取验证码'; // 恢复按钮文本为默认值
			             }
			           }
		
	}
	}
</script>

<style lang="scss">
.center-container {
	display: flex;
	  justify-content: center;
	  align-items: center;
	  height: 95.9vh;
	  
	background-image: url("../../static/login.jpg");
	// background-image: url("https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2167017148.jpg");
	background-repeat: no-repeat;
	background-size: 100% 100%;
	background-position: bottom;
}
.login_container {
	width: 30%; /* 设置矩形框的宽度，可以根据需要进行调整 */
	padding: 10px;
	border-radius: 8px;
	background-color: ghostwhite;
	
}
.byEmail {
	height: 200px;
}

/* 登录按钮 */
.login-button {
	background-color: #a0deaa;
	transition: background-color 0.3s; /* 添加过渡效果 */
}
.login-button:hover {
	background-color: #82c98f; /* 鼠标停留时的背景颜色 */
}
.login-button:active {
	background-color: #5e9e68; /* 鼠标点击时的背景颜色 */
}

.code-button {
	background-color: #ffffff;
	transition: background-color 0.3s; /* 添加过渡效果 */
}
.code-button:hover {
	background-color: #ebebeb; /* 鼠标停留时的背景颜色 */
}
.code-button:active {
	background-color: #e2e2e2; /* 鼠标点击时的背景颜色 */
}

.tips {
	display: flex;
	  justify-content: space-between;
}

</style>
