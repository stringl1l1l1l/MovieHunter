<template>
	<view name="referrer" content="no-referrer">
		
		<u-cell-group>
			<u-cell title="个人头像" >
				<template #right-icon>
										<u-avatar
											shape="square"
											size="35"
											:src="userinfo.avatar"
											customStyle="margin: -3px 5px -3px 0"
										></u-avatar>
									</template>
			</u-cell>
			<u-cell title='用户名' :value='userinfo.username' @click="changeUsername"></u-cell>
			<u-cell title="邮箱地址" :value="userinfo.email" @click="changeEmail"></u-cell>
			<u-cell title="收藏夹" arrow-direction="left" @click="showFavorite"></u-cell>
		</u-cell-group>
		<u-button class="button" text="修改密码" @click="resetPassword"></u-button>
		<u-button class="button" type="primary" @click="showLikes">查看点赞</u-button>
		<u-button class="button" type="primary" @click="showComments">查看评论</u-button>
		<u-button class="button" type="warning" @click="logout">退出登录</u-button>
		<u-button class="button" type="warning" @click="deleteUser">注销账号</u-button>
		<u-popup class="popup" :show="popupVisible" @close="closePopup">
			
		  <u-input class="input-field" v-model="inputText" placeholder="请输入新用户名"></u-input>
		  
		  <u-button class="submit-button" type="primary" @click="submitText">提交</u-button>
		</u-popup>	
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				userinfo:{},
				newEmail:'',
				isChangeShow:false,
				password:'',
				likes:[],
				comments:[],
				page:0,
				loadMore:true,
				inputText: '' ,// 用于绑定用户输入的文字
				popupVisible: false, // 控制弹窗显示与隐藏
				
						
					
				}
			
		},
		onLoad(){
			var that = this
			const value = uni.getStorageSync('token');
			if(value)
			{
					  that.token=value
					  console.log(that.token)
			}else{
					  uni.showToast({
					  	icon:Error,
					  	title:"登录校验失败，请重新登录"
					  })
					  uni.redirectTo({
					   url:'/pages/LoginByEmaiL/LoginByEmaiL'
					  })
			}
			console.log(this.token)
			uni.request({
				url:this.$BASE_URL.BASE_URL+'user/parseToken/'+that.token,
				method:'GET',
				success(res) {
					if(res.data.code==200)
					{
						console.log(res)
						that.userinfo = res.data.data
						if(that.userinfo.username==null){
							that.userinfo.username = 'MH用户' + that.userinfo.userId.slice(-5,-1)
						}
						
					}else{
						console.log(res)
					}
			},
			fail(res) {
				console.log(res)
				uni.showToast({
					title:"网络出错了",
					icon:'fail'
				})
			}
			
			
			
		})
	},
	methods:
	{
			    closePopup() {
			      this.popupVisible = false; // 关闭弹窗
			    },
		submitText(){
			var that = this
			uni.request({
				url:this.$BASE_URL.BASE_URL + 'user/updateUserById',
				method:'PUT',
				header:{token:that.token},
				data:{
					userId:that.userinfo.userId,
					email:that.userinfo.email,
					username:that.inputText,
					password:that.userinfo.password
				},
				success(res) {
					if(res.data.code==200)
					{
						console.log(res)
						uni.showToast({
							title:'更改成功'
						})
						that.userinfo.username = that.inputText
						that.inputText = ''
						 this.popupVisible = false;
					}else{
						uni.showToast({
							title:res.data.message
						})
					}
					
				}
			})
			
		},
		changeUsername(){
			this.popupVisible = true; // 打开弹窗
		},
		showLikes()
		{
			var that = this
			console.log(that.userinfo.userId)
			uni.navigateTo({
				url:'/pages/comments/myLikes?userId='+that.userinfo.userId
			})
		},
		
		showComments()
		{
			var that = this
			console.log(that.userinfo.userId)
			uni.navigateTo({
				url:'/pages/comments/myComments?userId='+that.userinfo.userId
			})
		},
		resetPassword(){
			uni.navigateTo({
				url:'/pages/findPassword/findPassword'
			})
		},
		deleteUser()
		{
			console.log(this.userinfo.userId)
			var that = this
			uni.showModal({
			  title: '确认注销用户？',
			  content:'该操作不可撤销！',
			  success(res) {
			    if (res.confirm) {
			      uni.request({
			        url: that.$BASE_URL.BASE_URL + 'user/deleteUserById/' + that.userinfo.userId,
			        method: 'DELETE',
			        header: { token: that.token },
			        success(res) {
			          if (res.data.code == 200) {
			            uni.showToast({
			              title: '注销成功'
			            });
			          }
			         uni.clearStorageSync()
					  that.userinfo = {}
			          uni.redirectTo({
			          	 url:'/pages/LoginByEmaiL/LoginByEmaiL'
			          })
			        },
			        complete(res) {
			          console.log(res);
			        }
			      });
			    }
			  }
			});
		},
		showFavorite(){
			uni.navigateTo({
				url:'/pages/favorite/favorite?userId='+this.userinfo.userId
			})
		},
		changeEmail()
		{
			this.isChangeShow = true
		},
		logout()
		{
			var that=this
			uni.request({
				url:this.$BASE_URL.BASE_URL+'logOut',
				method:'GET',
				header:{token:that.token},
				success(res) {
					if(res.data.code==200)
					{
						uni.clearStorageSync()
						that.userinfo = {}
						uni.redirectTo({
							 url:'/pages/LoginByEmaiL/LoginByEmaiL'
						})
					}else{
						uni.redirectTo({
							 url:'/pages/LoginByEmaiL/LoginByEmaiL'
						})
					}
				},
				complete(res) {
					console.log(res)
				}
			})
		}
		
	}
	}
</script>

<style lang="scss">
.button {
  
  margin-right: 20px;
}

</style>
