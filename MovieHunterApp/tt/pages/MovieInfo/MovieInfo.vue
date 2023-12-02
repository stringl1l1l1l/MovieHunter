<template>
	<view name="referrer" content="no-referrer">
		<img  style="width: 30%; height: 30%;" :src='movieInfo.cover' onerror="this.src='http://123.60.136.40:8081/MovieHunterWeb/static/movie.jpeg';this.οnerrοr=null"></img>
		
		<u-cell-group>
			<u-cell title="电影名" :value="movieInfo.name"/>
		      <u-cell title="导演" :value="movieInfo.directors" />
		      <u-cell title="演员" :value="movieInfo.actors" />
		      <u-cell title="时长"  :value="movieInfo.duration + '分钟'" />
		      <u-cell title="类型" :value="movieInfo.genres" />
		      <u-cell title="故事梗概" :value="movieInfo.storyline">
		      </u-cell>
		      <u-cell title="评分" :value="movieInfo.score" />
			   <u-cell title="上映时间" :value="movieInfo.releaseDate" />
		    </u-cell-group>
			<u-row justify="center">
				<u-col span="4">
					<u-button @click="showComments"> 查看评论</u-button>
				</u-col>
			</u-row>
			
	</view>
</template>

<script<script>
	export default {
		data() {
			return {
				token:'',
				movieInfo:{},
				movieId:0
			}
		},
		methods: {
			favorite()
			{
				
			},
			showComments()
			{
				console.log(this.movieId)
				uni.navigateTo({
					url:'/pages/comments/comments?movieId='+this.movieId,
					
				})
			}
			
		},
		onLoad(e) {
			var that = this
			var value = uni.getStorageSync('token');
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
		}
	}
</script>

<style>

</style>
