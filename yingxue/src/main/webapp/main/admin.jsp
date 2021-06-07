<%@page isELIgnored="false" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

        <script>
            $(function(){ //延迟加载
                //初始化jqGrid
                $("#admintab").jqGrid({
                    url : "${path}/admin/queryPage",
                    editurl:"${path}/admin/change",   //指定增删改操作的url   添加oper=add  修改oper=edit  删除oper=del
                    datatype : "json",
                    mtype : "post",
                    rowNum:2,
                    rowList : [ 1,3,5,20, 30 ],
                    pager : '#adminpage',
                    viewrecords : true,  //是否展示总条数
                    caption : "用户数据",
                    styleUI:"Bootstrap",
                    autowidth:true,
                    height:"auto",
                    //sortname : '',  //排序的字段
                   // sortorder : "desc",  //排序的方式
                    colNames : [ 'ID', '用户名','密码', '状态', '等级', '盐'],
                    colModel : [
                        {name : 'id',width : 55},
                        {name : 'username',editable:true,width : 90},
                        {name : 'password',editable:true,width : 100},
                        {name : 'status',editable:true,width : 100,align : "center",
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
                        {name : 'level',editable:true,width : 100,},
                        {name : 'salt',editable:true,width : 100}
                        /*{name : 'status',editable:true,width : 100},
                        {name : 'registTime',width : 80,align : "right"}*/
                        /*{name : 'deptId',editable:true,width : 150,edittype:"select",editoptions:{dataUrl:"${path}/dept/queryAll"},  //editoptions:{value:"男:男生;女:女孩"}
                            formatter:function(cellvalue, options, rowObject){
                                //console.log(rowObject);
                                return rowObject.depts.name;
                            }
                        }*/
                    ]
                }).navGrid("#adminpage", //开启增删改查操作  指定分页工具栏的位置
                    {edit:true,edittext:"修改",del:true,deltext:"删除",add:true,addtext:"添加",search:true,searchtext:"搜索"},  //是否展示增删改查按钮  {},都展示
                    {
                        closeAfterEdit:true
                    },  //修改之后的额外操作
                    {   //在执行添加成功之后进入该括号执行
                        closeAfterAdd:true //执行成功之后关闭对话框
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
                    $.post("${path}/admin/updateStatus",{"id":id,"status":"1"},function(data){
                        //刷新表单
                        $("#admintab").trigger("reloadGrid");
                    })
                }else{
                    $.post("${path}/admin/updateStatus",{"id":id,"status":"0"},function(data){
                        //刷新表单
                        $("#admintab").trigger("reloadGrid");
                    })
                }
            }
        </script>

        <%--面板--%>
        <div class="panel panel-danger">
            <%--面板头--%>
            <div class="panel panel-heading">用户信息展示</div>

            <%--面板内容--%>
            <div class="panel panel-body">
                <%--初始化jqGrid--%>
                <table id="admintab" />

                <%--分页工具栏--%>
                <div id="adminpage" />
            </div>

        </div>