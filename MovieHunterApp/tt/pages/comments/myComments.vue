<template>
	<view>
		<view v-if="isNull">
			<view>该用户暂无评论</view>
			</view>
			<view v-if="!isNull" class="comment-list" v-for="comment in comments" :key="comment.commentId">
				<view class="comment-info" @click="showInfo(comment.commentId)" >
					<u-row>
						   <u-col span="10"><view class="comment-content">{{ comment.content }}</view></u-col>
						   <u-col span="2">
							   <u-icon v-if="comment.isOwned" name="trash" @click="deleteComment(comment.commentId)"></u-icon>
						   </u-col>
					   </u-row>
					
					<u-row justify="between">
						<u-col span="4">
							<u-rate  :value="comment.rating" :active-color="'#FFD700'" :inactive-color="'#CCCCCC'" :size="20" readonly></u-rate>
						</u-col>
						<u-col span="3">
							<u-row>
								<text style="font-size: 5px;">有{{comment.votes}}人觉得很赞</text>
							</u-row>
						</u-col>
					</u-row>
					 <view class="divider" ></view>
					</view>
				</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				userId:'',
				comments:[],
				loadMore:true,
				page:0,
				isNull:true
			}
		},
		onLoad(e) {
			
			var that =this
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
			console.log(e.userId)
			that.userId = e.userId
			this.loadComments()
		},
		methods: {
			showInfo(e){
				let index = this.comments.findIndex( item => item.commentId == e);
				let movieId = this.comments[index].movieId
				uni.navigateTo({
					url:'/pages/MovieInfo/MovieInfo?movieId='+movieId
				})
			},
			getMovieName(e){
				var name = ''
				var that = this
				uni.request({
					url:this.$BASE_URL.BASE_URL + 'movie/findMovieById/' + e,
					method:'GET',
					header:{token:that.token},
					success(res) {
						name = res.data.data.name
					}
				})
				return name
			},
				loadComments(){
							var that = this
							var comments = []
							this.page++
							uni.request({
								url:this.$BASE_URL.BASE_URL+ 'comment/findCommentsByUserId/'+that.userId+'?pageNum='+that.page+'&pageSize=7',
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
											// for (var i = 0; i < comments.length; i++) {
												
											// 	 comments[i]['name'] = that.getMovieName(comments[i].movieId)
											// }
											that.comments = that.comments.concat(comments)
										}
										if(res.data.data.pages == that.page )
										{
											that.loadMore = false
										}else{
											that.loadMore = true
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
						}
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
	.comment-info {
	  
	
	  text-align: center; /* 居中显示评论信息 */
	  position: relative;
	}
	.comment-content {
	  text-align: center; /* 居中显示评论内容 */
	  font-weight: bold;
	}
	.centered-rate {
	  display: block;
	  margin: 0 auto; /* 将评分的星星居中显示 */
	}
	.divider {
	  height: 1px;
	  background-color: #ccc;
	  margin: 10px;
	}
</style>