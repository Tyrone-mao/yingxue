<%@page isELIgnored="false" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script src="${path}/js/echarts.js"></script>
<script type="text/javascript">
    $(function(){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        $.get("${path}/user/getUserDatas",function(data){

            // 指定图表的 配置项 和数据
            var option = {
                title: {
                    text: '用户信息统计图',  //设置主标题
                },
                tooltip: {},  //鼠标提示
                legend: {
                    data:['男','女']  //选项卡
                },
                xAxis: {
                    data: data.month  //横轴数据
                },
                yAxis: {},  //纵轴数据
                series: [{
                    name: '男',  //指定选项卡
                    type: 'bar',  //图标类型  bar 柱状图  line 折线图
                    data: data.boys  //数据
                },{
                    name: '女',  //指定选项卡
                    type: 'bar',  //图标类型  bar 柱状图  line 折线图
                    data: data.girls  //数据
                }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        },"json");
    })
</script>

<div align="center"><!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;" ></div>
</div>
