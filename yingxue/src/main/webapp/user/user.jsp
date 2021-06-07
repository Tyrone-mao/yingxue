<%@page isELIgnored="false" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

        <script>
            $(function(){ //延迟加载
                //初始化jqGrid
                $("#usertab").jqGrid({
                    url : "${path}/user/queryPage",
                    editurl:"${path}/user/change",   //指定增删改操作的url   添加oper=add  修改oper=edit  删除oper=del
                    datatype : "json",
                    mtype : "post",
                    rowNum:2,
                    rowList : [ 1,3,5,20, 30 ],
                    pager : '#userpage',
                    viewrecords : true,  //是否展示总条数
                    caption : "用户数据",
                    styleUI:"Bootstrap",
                    autowidth:true,
                    height:"auto",
                    //editurl:'${pageContext.request.contextPath}/user/',//设置编辑表单提交路径
                    //sortname : '',  //排序的字段
                   // sortorder : "desc",  //排序的方式
                    colNames : [ 'ID', '姓名','电话', '头像', '签名', '微信','状态','创建时间','性别','城市'],
                    colModel : [
                        {name : 'id',width : 55},
                        {name : 'name',editable:true,width : 90},
                        {name : 'phone',editable:true,width : 100,},
                        {name : 'headImg',index:'headImg',edittype:"file",editable:true,editoptions: {enctype: "multipart/form-data"},
                                        formatter:function (value,option,rows) {
                                        return "<img  style='width:30%;height:10s%;' " + "src='"+rows.headImg+"'/>";}
                        },
                        {name : 'sign',editable:true,width : 100},
                        {name : 'wechat',width : 100},
                        {name : 'status',width : 100,
                            formatter:function(cellvalue, options, rowObject){
                                if(cellvalue==1){
                                    //正常用户  点击冻结   //updateStatus("+rowObject.id+","+rowObject.status+")
                                    return "<button class='btn btn-success' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")' >冻结</button>";
                                }else{
                                    //已冻结用户  点击解除冻结
                                    return "<button class='btn btn-danger' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")'>解除冻结</button>";
                                }
                            }

                        },

                        {name : 'registTime',width : 80,align : "right"},
                        {name : 'sex',width : 80,align : "right",editable:true},
                        {name : 'city',width : 80,align : "right",editable:true}
                        /*{name : 'deptId',editable:true,width : 150,edittype:"select",editoptions:{dataUrl:"${path}/dept/queryAll"},  //editoptions:{value:"男:男生;女:女孩"}
                            formatter:function(cellvalue, options, rowObject){
                                //console.log(rowObject);
                                return rowObject.depts.name;
                            }
                        }*/
                    ]
                }).navGrid("#userpage", //开启增删改查操作  指定分页工具栏的位置
                    {edit:true,edittext:"修改",del:true,deltext:"删除",add:true,addtext:"添加",search:true,searchtext:"搜索"},  //是否展示增删改查按钮  {},都展示
                    {
                        closeAfterEdit:true,
                        afterSubmit:function (data) {
                            //上传添加的图片
                            var statu=data.responseJSON.statu;
                            alert(statu)
                            if(statu){
                                $.ajaxFileUpload({
                                    url:"${path}/user/upload",
                                    fileElementId:"headImg",
                                    data:{"id":data.responseJSON.id},
                                    type:"post",
                                    dataType:"json",
                                    success:function (data) {
                                        alert(data.massge)
                                        $("#usertab").trigger("reloadGrid");  //刷新表格
                                    }
                                });
                            }
                            return "12312";
                        }

                    },  //修改之后的额外操作
                    {   //在执行添加成功之后进入该括号执行
                      //  closeAfterAdd:true //执行成功之后关闭对话框
                        //添加的额外控制
                        closeAfterAdd:close,    //关闭添加框
                        afterSubmit:function (data) {   //上传添加的图片
                                $.ajaxFileUpload({
                                    url:"${path}/user/upload",
                                    fileElementId:"headImg",
                                    data:{"id":data.responseJSON.id},
                                    type:"post",
                                    dataType:"json",
                                    success:function (data) {
                                        alert(data.massge)
                                        $("#usertab").trigger("reloadGrid");  //刷新表格
                                    }
                                });
                            return "12312";
                        }
                    },  //添加之后的额外操作
                    {
                        closeAfterDel: true
                    }   //删除之后的额外操作
                    /*{
                        closeAfterSearch: true,
                        sopt : [ 'cn', 'bw', 'eq', 'ne', 'lt', 'gt', 'ew' ] //设置搜索的方式
                    } */ //搜索的额外配置
                );
            });

            //修改状态的方法
            function updateStatus(id,status){
                //修改数据最少要几个参数   id   修改的字段(状态)
                if(status==0){
                    $.post("${path}/user/updateStatus",{"id":id,"status":"1"},function(data){
                        //刷新表单
                        $("#usertab").trigger("reloadGrid");
                    })
                }else{
                    $.post("${path}/user/updateStatus",{"id":id,"status":"0"},function(data){
                        //刷新表单
                        $("#usertab").trigger("reloadGrid");
                    })
                }
            }

        $("#btn").click(function (){
            $.post("${path}/user/getPhoneCode",$("#from").serialize(),function (data){
                if (data.statu==100){
                    alert(data.massge)
                }else {
                    alert(data.massge)
                }
            },"JSON"
            )})


        </script>

        <%--面板--%>
        <div class="panel panel-danger">
            <%--面板头--%>
            <div class="panel panel-heading">用户信息展示</div>

                <%--设置标签页--%>
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#home" >用户信息</a></li>
                </ul>
                <form class="form-inline" id="from" >
                    <div class="form-group">
                        <label class="sr-only" for="phone">请输入手机号</label>
                        <input type="text" class="form-control" id="phone" placeholder="phone" name="phone">
                    </div>
                    <button type="button" class="btn btn-default" id="btn">发送验证码</button>
                </form>

            <%--面板内容--%>
            <div class="panel panel-body">
                &emsp;&emsp;<button class="btn btn-success">导出用户信息</button>
                &emsp;&emsp;<button class="btn btn-info">导入用户信息</button>
                &emsp;&emsp;<button class="btn btn-warning">测试按钮</button><br><br>
                <%--初始化jqGrid--%>
                <table id="usertab" />

                <%--分页工具栏--%>
                <div id="userpage" />
            </div>

        </div>