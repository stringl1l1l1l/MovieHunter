<template>
	<view>
		<view v-if="isNull">
			<view>该电影暂无评论</view>
			<button class="comment-button" @click="openPopup" size="mini">发表第一条评论</button>
			</view>
			
		<view v-if="!isNull" class="comment-list" v-for="comment in comments" :key="comment.commentId">
		     
		              <view class="comment-info" >
		                      <view>评论用户: {{ comment.username }}</view>
		                      <view>评论时间: {{ comment.commentTime }}</view>  
							   <u-row>
								   <u-col span="10"><view class="comment-content">{{ comment.content }}</view></u-col>
								   <u-col span="2">
									   <u-icon v-if="comment.isOwned" name="trash" @click="deleteComment(comment.commentId)"></u-icon>
								   </u-col>
							   </u-row>
		                    
							<u-row justify="between">
								<u-col span="4">
									<u-rate class="centered-rate" :value="comment.rating" :active-color="'#FFD700'" :inactive-color="'#CCCCCC'" :size="20" readonly></u-rate>
								</u-col>
								<u-col span="1">
									<u-row>
										<u-icon class="like-icon" :name="comment.isVoted ? 'thumb-up-fill' : 'thumb-up'" @click="toggleLike(comment.commentId)" />
									</u-row>
									<u-row>
										<text style="font-size: 5px;">有{{comment.votes}}人觉得很赞</text>
									</u-row>
								</u-col>
							</u-row>
							 
				 
							 <view class="divider" ></view>
		                  </view>
						  </view>
		      
		   
			<u-icon name="arrow-up-fill" class='topback' @click="topback"></u-icon>
			<button class="comment-button" @click="openPopup" v-if="!isNull" size="mini">发表评论</button>
		  
		    <u-popup class="popup" :show="popupVisible" @close="closePopup">
				<u-rate  v-model="ratingValue" :max="5" :readonly="false"></u-rate>
		      <u-input class="input-field" v-model="inputText" placeholder="请输入评论"></u-input>
			  
		      <u-button class="submit-button" type="primary" @click="submitText">提交</u-button>
		    </u-popup>	
			 <view v-if="!loadMore" bg-color="#f3f3f3">没有更多了</view >
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				movieId:0,
				comments:[],
				isNull:true,
				popupVisible: false, // 控制弹窗显示与隐藏
				inputText: '' ,// 用于绑定用户输入的文字
				ratingValue: 0,
				likeIconPath:'../..//static/liked.png',
				  isLiked: false,
				  page:1,
				  pageSize:7,
				  loadMore:true,
				  
				  
			}
		},
		methods: {
			topback(){
				console.log('back')
					  uni.pageScrollTo({
					  				scrollTop:0,   // 滚动到页面的目标位置  这个是滚动到顶部, 0 
					  				duration:300  // 滚动动画的时长
					  			})
			},
			loadComments(){
				var that=this
				
				uni.request({
					url:this.$BASE_URL.BASE_URL+'comment/findCommentsByMovieId/'+that.movieId+'?pageNum='+that.page+'&pageSize='+that.pageSize,
					method:'GET',
					header:{
						token:that.token
						},
						success(res) {
							if(res.data.code==200)
							{
								if(parseInt( res.data.data.total)>0)
								{
									that.isNull = false
									console.log(that.page)
									if(that.page<=res.data.data.pages)
									{
										that.comments = that.comments.concat(res.data.data.records)
										that.page += 1
									}
									if(that.page > res.data.data.pages)
									{
										that.loadMore = false
									}
									
								}
								console.log(res)
								console.log(that.comments)
								uni.hideLoading()
								
								
							}else if(res.data.code==401){
								console.log(res)
								uni.redirectTo({
									 url:'/pages/LoginByEmaiL/LoginByEmaiL'
								})
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
			deleteComment(e)
			{
				var that=this
				let index = this.comments.findIndex( item => item.commentId == e);
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
				            uni.showToast({
				              title: '删除成功'
				            });
							that.comments.splice(index,1)
							
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
								that.comments[index].isVoted = false
								that.comments[index].votes -= 1
							}else{
								uni.showToast({
									title:res.data.message,
									icon:'none'
								})
								
								console.log(res)
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
								console.log('点赞失败')
							}
						},
						complete(res) {
							console.log(res)
						}
					})
				}
				
				
			},
			 openPopup() {
			      this.popupVisible = true; // 打开弹窗
			    },
			    closePopup() {
			      this.popupVisible = false; // 关闭弹窗
			    },
			    submitText() {
			      // 处理用户输入的文字
			      console.log('提交文字:', this.inputText);
				  var that = this
			      this.popupVisible = false; // 关闭弹窗
				  uni.request({
				  	url:this.$BASE_URL.BASE_URL+'comment/insertComment',
					header:{token:that.token},
					method:'PUT',
					data:{
						movieId:that.movieId,
						rating:that.ratingValue,
						content:that.inputText
					},
					success(res) {
						if(res.data.code==200){
							uni.showToast({
								title:'发布成功'
							})
							that.comments=[]
							that.page = 1
							that.loadComments()
						}else{
							uni.showToast({
								title:res.data.message,
								icon:'none'
							})
						}
					},
					complete(res) {
						console.log(res)
					}
				  })
			    }
			
		},
		onLoad(e) {
			var that = this
			that.comments = []
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
			console.log(e)
			that.movieId = parseInt(e.movieId)
			console.log(that.movieId)
			this.loadComments()
			
		},
		onReachBottom() {
			if(this.loadMore)
			{ 
				uni.showLoading({
					title:'加载中'
				})
				console.log('加载')
				this.loadComments()
			}
			
		
		},
		onPullDownRefresh(){
			this.comments = []
			this.page = 1
			this.loadComments()
			uni.stopPullDownRefresh()
		}
	}
</script>

<style>
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px; /* 调整评论之间的间距 */
  align-items: center; /* 将评论内容等信息居中显示 */
}
.comment-item {
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
}
.comment-info {
  

  text-align: center; /* 居中显示评论信息 */
  position: relative;
}
.divider {
  height: 1px;
  background-color: #ccc;
  margin: 10px;
}
.comment-content {
  text-align: center; /* 居中显示评论内容 */
  font-weight: bold;
}
.centered-rate {
  display: block;
  margin: 0 auto; /* 将评分的星星居中显示 */
}
.comment-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  position: fixed;
  bottom:10%;
  left:30%;
  right: 30%;
  
}
.comment-button:hover {
  background-color: #0056b3;
}
.like-icon {
  font-size: 24px;
  margin-right: 8px;
  color: #999;
}
.topback{
	 position: fixed;
	  right: 2rem;
	  bottom: 2rem;
	  size:25rpx;
	  color: coral;
}
</style>
