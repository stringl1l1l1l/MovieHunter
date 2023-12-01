<template>
	<view class="base">
		
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
		<view class="movie">
		<view class="movie-title">{{ movieInfo.name }}({{movieInfo.year}})</view>
		<view class="movie-item-top">
			<view class="movie-main"/>
			<img class="movie-poster" :src="movieInfo.cover" alt="无海报" onerror="this.src='http://123.60.136.40:8081/MovieHunterWeb/static/movie.jpeg';this.οnerrοr=null"></img>
			<u-cell-group>
				  <u-cell  :title="'【导演】: ' + movieInfo.directors" />
				  <u-cell  :title="'【主演】: ' + movieInfo.actors" />
				  <u-cell  :title="'【类型】: ' + movieInfo.genres" />
				  <u-cell  :title="'【制片国家/地区】: ' + movieInfo.regions" />
				  <u-cell  :title="'【语言】: ' + movieInfo.languages" />
				  <u-cell  :title="'【上映日期】: ' + movieInfo.releaseDate" />
				  <u-cell  :title="'【片长】: ' + movieInfo.duration + '分钟'" />
		    </u-cell-group>
			<view class="movie-score">
				{{ movieInfo.score }}分
			</view>
		</view>
		<view class="intro-title">
			剧情简介...... 
		</view>
		<view>{{movieInfo.storyline}}</view>
		<u-row justify="center">
			<!-- <u-col span=5>
				<u-button @click="favorite" class="button"> 加入收藏夹</u-button>
			</u-col> -->
			<!-- <u-col span=5>
				<u-button @click="showComments" class="button"> 展开评论</u-button>
			</u-col> -->
		</u-row>
		</view>
		</view>
		
		<!-- 弹窗内容 -->
		<view v-if="popupVisible" class="overlay">
			<div class="sub-window-create">
				<div @click="closePopup()" class="close-button">&times;</div>
				  <u-rate  v-model="ratingValue" :max="5" :readonly="false" style="margin-top: 10pt;"></u-rate>
				  <u-input class="input-field" v-model="inputText" placeholder="请输入评论"></u-input>
				  
				  <u-button class="submit-button" type="primary" @click="submitText">提交</u-button>
				
			</div>
		</view>
		
		<view class="comment-main">
			<view v-if="isNull">
				<view style="color: #919191;">该电影暂无评论</view>
				<u-button class="open-button" @click="openPopup">发表第一条评论</u-button>
			</view>
			<u-list v-if="!isNull" class="comment-list">
				<u-button class="open-button" @click="openPopup" v-if="!isNull">发表评论</u-button>
			  <view v-for="comment in comments" :key="comment.commentId">
					  <view class="comment-info">
							<u-row justify="between">
								<u-col span="6">
									<view class="row-info">
										<view style="font-weight: bold;">用户: {{ comment.commentId }} </view>
										<u-rate class="centered-rate" :value="comment.rating" :active-color="'#FFD700'" :inactive-color="'#CCCCCC'" :size="20" readonly></u-rate>
										<view style="color: #919191;">{{ comment.commentTime }}</view>
									</view>
								</u-col>
								<u-col span="1">
									<u-icon v-if="!comment.isVoted" class="like-icon" :name="isLiked ? 'thumb-up-fill' : 'thumb-up'" @click="toggleLike(comment.commentId)" />
									<u-icon v-if="comment.isVoted" class="like-icon-voted" :name="isLiked ? 'thumb-up-fill' : 'thumb-up'" @click="toggleLike(comment.commentId)" />
									<text style="font-size: 10pt;">{{comment.votes}}</text>
								</u-col>
								<u-col span="1">
									<u-icon v-if="comment.isOwned" name="trash" @click="deleteComment(comment.commentId)"></u-icon>
								</u-col>
							</u-row>
							<u-row>
							   <u-col span="10"><view class="comment-content">{{ comment.content }}</view></u-col>
							   
							</u-row>
							 
					</view>		 
							 <view class="divider"></view>
				</view>
			  <u-button v-if="showMore" class="open-button" @click="loadComments()">加载更多</u-button>
			</u-list>
			
		</view>
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token:'',
				movieInfo:{},
				movieId:0,
				comments:[],
				isNull:true,
				popupVisible: false, // 控制弹窗显示与隐藏
				inputText: '' ,// 用于绑定用户输入的文字
				ratingValue: 0,
				isLiked: false,
				page:1,
				pageSize:7,
				showMore: true,
			}
		},
		onLoad(e) {
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
			})
			console.log(e)
			this.movieId =parseInt(e.movieId)
			console.log(this.movieId)
			uni.request({
				url:this.$BASE_URL.BASE_URL+'movie/findMovieById/'+that.movieId,
				method:'GET',
				header:{
					token:that.token
					},
					success(res) {
						if(res.data.code==200)
						{
							console.log(res)
							that.movieInfo = res.data.data
							that.loadComments()
						}else if(res.data.code==401){
							console.log(res)
		    				uni.redirectTo({
		    					url:'/pages/Login/LoginByEmail'
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
				console.log(this.page)
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
								console.log('加载')
								if(parseInt( res.data.data.total)>0)
								{
									that.isNull = false
									
								}else{
									that.isNull = true
								}
								console.log(that.page * that.pageSize)
								console.log((parseInt(res.data.data.total) + that.pageSize - 1))
								if((parseInt( res.data.data.total) + that.pageSize - 1)<=(that.page * that.pageSize)){
									that.showMore = false
									if(that.page!=1){
										uni.showToast({
											title:"已经到底",
											icon:'error'
										})
									}
									that.comments = that.comments.concat(res.data.data.records)
							
								}else{
									that.comments = that.comments.concat(res.data.data.records)
						
									if((parseInt( res.data.data.total) + that.pageSize - 1)<((that.page+1) * that.pageSize)){
										that.showMore = false
									}
								}
								that.page += 1
								console.log(res)
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
							that.page=1
							that.comments=[]
							that.showMore=true
							that.loadComments()
				            uni.showToast({
				              title: '删除成功'
				            });
				          }else{
							  uni.showToast({
							    title: res.data.message,
								icon: 'error'
							  });
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
								    title: res.data.message,
									icon: 'error'
								});
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
								    title: res.data.message,
									icon: 'error'
								});
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
			  that.popupVisible = false; // 关闭弹窗
			  uni.request({
				url:that.$BASE_URL.BASE_URL+'comment/insertComment',
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
						uni.request({
							url:that.$BASE_URL.BASE_URL+'movie/findMovieById/'+that.movieId,
							method:'GET',
							header:{
								token:that.token
								},
								success(res) {
									if(res.data.code==200)
									{
										console.log(res)
										that.movieInfo = res.data.data
										that.page=1
										that.comments=[]
										that.showMore=true
										that.loadComments()
									}else if(res.data.code==401){
										console.log(res)
										uni.redirectTo({
											url:'/pages/Login/LoginByEmail'
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
					}else{
						uni.showToast({
						    title: res.data.message,
							icon: 'error'
						});
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

<style>
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
.sub-window-create {
	position: relative;
	width: 300px;
	 height: 100px;
	 background-color: #fff;
	 border: 1px solid #ccc;
	 padding: 16px;
}
.row-info {
	display: flex;
	justify-content: center;
}
.comment-list {
	margin-top: 30pt;
	width: 60%;
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
	
  /* font-weight: bold; */
  margin-bottom: 10px;
  text-align: center; /* 居中显示评论信息 */
  position: relative;
}
.divider {
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
}
.comment-content {
	margin-top: 10pt;
  text-align: start; /* 居中显示评论内容 */
}
.centered-rate {
  display: block;
  margin: 0 auto; /* 将评分的星星居中显示 */
}
.comment-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}
.comment-button:hover {
  background-color: #0056b3;
}
.like-icon {
  color: #999;
}
.like-icon-voted {
	width:30%;
  background-color: #ffff7f;
}
.button {
	background-color: #a0deaa;
	transition: background-color 0.3s; /* 添加过渡效果 */
}
.button:hover {
	background-color: #82c98f; /* 鼠标停留时的背景颜色 */
}
.button:active {
	background-color: #5e9e68; /* 鼠标点击时的背景颜色 */
}

.movie-score {
	font-size: 48px;
	font-weight: bold;
}
.intro-title {
	margin-top: 10pt;
	margin-bottom: 10pt;
	font-size: 18pt;
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
.base {
	background-color: #f5f5f5;
}
.main {
	display: flex;
	justify-content: center;
	background-color: #f5f5f5;
}
.comment-main {
	display: flex;
	justify-content: center;
	background-color: #f5f5f5;
}
.movie {
	width: 60%;
	background-color: #f5f5f5;
}
.movie-poster {
	width: 210pt;
	height: 300pt;
}
.movie-item-top {
	display: flex;
	justify-content: start;
}
.movie-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10pt;
  margin-top: 10pt;
}
</style>
