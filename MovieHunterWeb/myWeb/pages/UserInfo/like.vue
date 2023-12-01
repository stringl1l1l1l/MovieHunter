<template>
	<view>
		<meta name="referrer" content="no-referrer" />
		<div>
		        <div class="header">
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/home.png" class="img_btn">
		        						 <text class="link_btn"  @click="handleClick('recommendations/recommendations')">首页</text>
		                    </nav>
		            </div>
		            <div class="box">
		                <nav class="nav_link">
		                    <img src="../../static/movieTag.png" class="img_btn">
		        						 <text class="link_btn"  @click="handleClick('favorite/favorite')">收藏夹</text>
		                </nav>
		        			 
		                <nav class="nav_link">
		                    <img src="../../static/user.png" class="img_btn">
		                    <text class="link_btn" @click="handleClick('UserInfo/UserInfo')">个人主页</text>
		                </nav>
		        			 
		        			 <nav class="nav_link">
		        			     <img src="../../static/exit.png" class="img_btn">
		        			     <text class="link_btn" @click="handleClick('Login/LoginByEmail')">退出</text>
		        			 </nav>
		            </div>
		        </div>
		    </div>
		<view class="main">
		<view class="comments-list">
			<view v-if="isNull">
				<view>该用户暂无点赞</view>
			</view>
		    <view v-if="!isNull" v-for="comment in comments" :key="comment.commentId" class="comment-item">
		      <view class="container" >
		        <view class="left-section">
					<text class="text" >{{ comment.commentTime }}</br></text>
					<text class="text" >{{ comment.content }}</text>
					<u-rate  :value="comment.rating" :active-color="'#FFD700'" :inactive-color="'#CCCCCC'" :size="20" readonly></u-rate>
				</view>
				  <view class="right-section">
					  <u-button class="edit-button" @click="showInfo(comment.commentId)" text="查看详情"></u-button>
						<u-button class="remove-button" text="取消点赞" type="warning"  @click="toggleLike(comment.commentId)"></u-button> 
				  </view>
		      </view>
			  
		    </view>
			<u-button v-if="showMore && !isNull" class="open-button" @click="loadComments()">加载更多</u-button>
		  </view>
		  
		  
		</view> 
	</view>
</template>

<script>
	export default {
		data() {
			return {
				comments:[],
				userId:'',
				movies:[],
				isMoviesShow:false,
				token:'',
				selectcommentId:0,
				userinfo:{},
				showChange:false,
				selectMovieId:0,
				 
				showPopup: false,
				inputText: '',
				isCreateShow: false,
				isErrorShow: false,
				page:0,
				isNull:true,
				showMore: true,
			}
		},
		onLoad(){
			var that = this
			uni.getStorage({
				key:'token',
				success(res) {
					that.token=res.data;
				},
					fail(){
						uni.showToast({
							icon:Error,
							title:"登录校验失败，请重新登录"
						})
						uni.redirectTo({
							url:'/pages/Login/LoginByEmail'
						})
					}
			}),
			console.log(this.token)
			uni.request({
				url:this.$BASE_URL.BASE_URL+'user/parseToken/'+that.token,
				method:'GET',
				success(res) {
					if(res.data.code==200)
					{
						console.log(res)
						that.userinfo = res.data.data
						that.userId = res.data.data.userId
						that.loadComments()
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
			}})
		},   
		methods: {
			handleClick(e){
				uni.redirectTo({
					url:'/pages/'+e
				})
			},
			loadComments(){
				if(this.showMore==false){
					return
				}
				var that = this
				var comments = []
				this.page++
				
				uni.request({
					url:this.$BASE_URL.BASE_URL+ 'comment/findVotedCommentsByCurUser?pageNum='+that.page+'&pageSize=7',
					method:'GET',
					header:{token:that.token},
					success(res) {
						if(res.data.code==200){
							console.log(res)
							comments = res.data.data.records
							console.log(comments)
							if(res.data.data.total==0){
								that.isNull = true
							}else{
								that.isNull = false
								that.comments = that.comments.concat(comments)
							}
							if(res.data.data.pages <= that.page)
							{
								if(that.page!=1){
									uni.showToast({
										title:"已经到底",
										icon:'error'
									})
								}
								that.showMore = false
							}else{
								that.showMore = true
							}
						}else{
							console.log(res)
							uni.showToast({
								title:res.data.message,
								icon:'none'
							})
						}
					},
					fail(res) {
						console.log(res)
						uni.showToast({
							title:"网络出错了",
							icon:'error'
						})
					}
				})
			},
			updateComments(e){
				this.page=0
				this.comments=[]
				this.showMore=true
				this.loadComments()
			}, 
			deleteComment(e) {
			  console.log(e);
			  var that = this;
			  uni.showModal({
			    title: '确认删除？',
			    success(res) {
			      if (res.confirm) {
			        uni.request({
			          url: that.$BASE_URL.BASE_URL + 'comment/deleteCommentById/' + e,
			          method: 'DELETE',
			          header: { token: that.token },
			          success(res) {
			            if (res.data.code == 200) {
							
							that.updateComments(e);
			              uni.showToast({
			                title: '删除成功'
			              });
			            }else{
							uni.showToast({
								title:res.data.message,
								icon: 'error'
							})
						}
			          },
			          complete(res) {
			            console.log(res);
			          }
			        });
			      }
			    }
			  });
			},
			showInfo(e){
				let index = this.comments.findIndex( item => item.commentId == e);
				let movieId = this.comments[index].movieId
				uni.navigateTo({
					url:'/pages/MovieInfo/MovieInfo?movieId='+movieId
				})
			},
			toggleLike(e)
			{
				var that = this
				let index = this.comments.findIndex( item => item.commentId == e);
				console.log(index)
				if(this.comments[index].isVoted)
				{
					uni.request({
						url:this.$BASE_URL.BASE_URL+'likes/cancelVoteByCommentId/'+e,
						method:'DELETE',
						header:{token:that.token},
						success(res) {
							if(res.data.code==200){
								console.log('取消成功')
								uni.showToast({
									title:'取消成功'
								})
								that.updateComments(e);
							}else{
								uni.showToast({
									title:res.data.message,
									icon: 'error'
								})
							}
						},
						fail(res) {
						          console.log(res);
						          uni.showToast({
						            title: '网络出错了',
						            icon: 'error'
						          });
						        }
					})
				}else{
					uni.request({
						url:this.$BASE_URL.BASE_URL+'likes/voteCommentById/'+e,
						header:{token:that.token},
						method:'GET',
						success(res) {
							if(res.data.code==200){
								console.log('点赞成功')
								that.comments[index].isVoted =true
								that.comments[index].votes += 1
							}else{
								uni.showToast({
									title:res.data.message,
									icon: 'error'
								})
							}
						},
						complete(res) {
							console.log(res)
						}
					})
				}
			},
		}
	}
</script>

<style>
.scrollable-list {
  overflow-y: auto;
  height: 100%;
}
.popup-content {
	margin-top: 20pt;
}
.main {
	display: flex;
	justify-content: center;
	background-color: #f5f5f5;
}
.close-button {
  position: absolute;
  top:1px;
  right: 8px;
  font-size: 36px;
  color: #999;
  cursor: pointer;
  /* background-color: #333; */
  border-width: 0%;
}
.close-button:hover {
  color: #333;
}
.overlay {
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.sub-window {
  position: relative;
 width: 300px;
  height: 315px;
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 16px;
}
.sub-window-create {
	position: relative;
	width: 300px;
	 height: 100px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}
.sub-window-big {
	position: relative;
	width: 600px;
	 height: 700px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}

.container {
  display: flex;
  align-items: center;
 
  padding: 10px;
 
}
.left-section {
  flex: 1;
}

.comments-list {
	background-color: #f5f5f5;
	width:50%
}
.remove-button {
  color: #fff;
  border: none;
  
  border-radius: 4px;
  cursor: pointer;
  min-width: 80px; /* 调整按钮的最小宽度 */
   margin-left: 10px; /* 将按钮推至右侧 */
  
}
.edit-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  
  border-radius: 4px;
  cursor: pointer;
  min-width: 80px; /* 调整按钮的最小宽度 */
   margin-left: 10px; /* 将按钮推至右侧 */
}
.comment-item {
  /* display: flex;
   flex: 1 1 200px;
  align-items: center; */
  justify-content: space-between;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.comment-info {
  display: flex;
  align-items: center;
}

.comment-name {
  margin-right: 10px;
  cursor: pointer;
}

.button {
  margin-left: 10px;
}

.movie-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  z-index: 0;
}

.movie-item {
  width: 100%;
  max-width: 600px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
.movie-item-small {
	width: 100%;
	border: 1px solid #e0e0e0;
	border-radius: 5px;
	margin-bottom: 20px;
	padding: 15px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	background-color: #fff;
}
.text {
  font-size: 16px;
  white-space: nowrap; /* 防止文字换行 */
  overflow: hidden; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
}
.movie-name {
  margin: 20px;
}
.right-section {
  display: flex;
  margin-left: 80px; /* 将右侧按钮推至右边缘 */
}
.header{
	background-image: url("../../static/title.png");
	background-repeat: no-repeat;
	background-size: 38% 100%;
	background-position: bottom;
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 70px;
	background-color:#edf4ed;
}
.box{
	display: flex;
	align-items: center;
}
.nav_link{
        display: flex;
        align-items: center;
        padding: 0 30px;
}
.link_btn{
	text-decoration:none;
	color: #7F7F7F;
	font-size: 25px;
	font-weight: normal;
}
.img_btn{
	width: 30px;
	height: 30px;
}
.title_btn{
	width: 40%;
	height: 40%;
}
.movie-list {
	  display: flex;
	  flex-direction: column;
	  align-items: center;
	  padding: 20px;
	  z-index: 0;
	}
.movie-item {
  width: 100%;
  max-width: 600px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

</style>
