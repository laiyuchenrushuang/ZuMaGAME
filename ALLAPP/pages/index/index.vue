//一个vue就是一个界面


<template>
	<div class="scroll-content">
		<p v-for="(item,index) in prizeList" class='itemList'>
			<view class="name1">{{item.name}}</view>
			<view class="class1" :class="{class2 :item.bj == 1}">{{item.time}}</view>
			<button class="btn" type="primary" @click="updateItemClick(item,index)">更新</button>
			<button class="btn" type="primary" @click="deleteItemClick(item,index)">删除</button>
		</p>
	</div>
</template>

<script>
	export default {
		name: 'complexTable',
		data() {
			return {
				prizeList: [{
						name: '暗之陵墓',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '人鱼海岛',
						time: '00:00:00',
						bj: 0,
						period: "1"
					},
					{
						name: '圣域领地',
						time: '00:00:00',
						bj: 0,
						period: "" + parseFloat(43 / 60)
					},
					{
						name: '天魔石窟BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '祖玛大厅BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '猪洞三层BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '妖山二层BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '森林三层BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '地下三层BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '海底海默BOSS',
						time: '00:00:00',
						bj: 0,
						period: "2"
					},
					{
						name: '幽冥BOSS',
						time: '00:00:00',
						bj: 0,
						period: "4"
					},
					{
						name: '幽冥精英',
						time: '00:00:00',
						bj: 0,
						period: "0.5"
					},
					{
						name: '雪域BOSS',
						time: '00:00:00',
						bj: 0,
						period: "3"
					},
				],
			
			}
		},

// 		created() {
// 			while (true) {
				
// 				// console.log("DEBUG");
// 			}
// 
// 		},
// 				watch: {
// 					data(oldValue, newValue){
// 						handler(newValue, oldValue) {
// 							for (let i = 0; i < this.prizeList.length; i++) {
// 								var item = this.prizeList[i];
// 								var h = new Date().getHours();
// 								var m = new Date().getMinutes();
// 								var s = new Date().getSeconds();
// 								var currentTime = parseInt(h) * 60 * 60 + parseInt(m) * 60 + parseInt(s);
// 							
// 								var itemTime = item.time.split(":")[0] * 60 * 60 + item.time.split(":")[1] * 60 + item.time.split(":")[2];
// 							
// 								if (itemTime - currentTime > 0 && itemTime - currentTime < 10 * 60) {
// 									this.bj = 1;
// 								}
// 								console.log("DEBUG");
// 							}
// 						},
// 					}
// 				},


		methods: {
			updateItemClick: function(item, index) {
				// 				var currentTime = this.getCurrentTime();
				// 				var itemTime = this.getItemBossTime(item);
				// 				
				// 				this.bj =0;
				item.bj = 1;
				if (parseInt(item.period) >= 1) {
					if (parseInt(new Date().getHours()) + parseInt(item.period) > 23) {
						this.prizeList[index].time = parseInt(new Date().getHours()) + parseInt(item.period) - 24 + ":" + new Date().getMinutes() +
							":" + new Date().getSeconds();
					} else {
						this.prizeList[index].time = parseInt(new Date().getHours()) + parseInt(item.period) + ":" + new Date().getMinutes() +
							":" + new Date().getSeconds();
					}
				} else {
					var min = parseInt(parseFloat(item.period) * 60);
					if ((new Date().getMinutes() + min) > 59) {
						if (parseInt(new Date().getHours()) + 1 > 23) {
							this.prizeList[index].time = "00" + ":" + parseInt(new Date().getMinutes() + min -
									60) +
								":" + new Date().getSeconds();
						} else {
							this.prizeList[index].time = parseInt(new Date().getHours()) + 1 + ":" + parseInt(new Date().getMinutes() + min -
									60) +
								":" + new Date().getSeconds();
						}

					} else {
						this.prizeList[index].time = new Date().getHours() + ":" + parseInt(new Date().getMinutes() + min) +
							":" + new Date().getSeconds();
					}
				}
				this.prizeList.sort(function(a, b) {
					var aStr = a.time;
					var bStr = b.time;
					if (aStr.split(":")[0] == bStr.split(":")[0]) {
						if (aStr.split(":")[1] == bStr.split(":")[1]) {
							return bStr.split(":")[2] - aStr.split(":")[2];
						} else {
							return bStr.split(":")[1] - aStr.split(":")[1];
						}

					} else {
						return bStr.split(":")[0] - aStr.split(":")[0];
					}
				})
			},
			deleteItemClick: function(item, index) {
				this.prizeList.splice(index, 1);
			},

			getCurrentTime: function() {
				return parseInt(new Date().getHours() * 60 * 60) + parseInt(new Date().getMinutes() * 60) + parseInt(new Date().getSeconds());
			},
			getItemBossTime: function(item) {
				var h = parseInt(time.split(":")[0]);
				var m = parseInt(item.time.split(":")[1]);
				var s = parseInt(item.time.split(":")[2]);
				return h * 60 * 60 + m * 60 + s;
			}
		}
	}
</script>
<style>
	.scroll-content {
		line-height: 50px;
		text-align: center;
	}

	.itemList {
		display: flex;
		flex-direction: row;
		margin-bottom: 5px;
		align-items: center;
	}

	.name1 {
		width: 100px;
		padding-right: 5px;
		font-size: 12px;
	}

	.class1 {
		flex-grow: 0.8;
		padding-right: 5px;
		font-size: 15px;
		color: #333333;
	}

	.class2 {
		flex-grow: 0.8;
		padding-right: 5px;
		font-size: 12px;
		color: #DD524D;
	}

	.btn {
		display: flex;
		width: 80px;
		height: 40px;
		border-radius: 5px;
		margin-right: 10px;
		padding-left: 28px;
		padding-top: 6px;
		font-size: 12px;
	}
</style>
