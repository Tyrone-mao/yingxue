<%@page isELIgnored="false" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

        <script>
            $(function(){ //延迟加载
                //初始化jqGrid
                $("#feedtab").jqGrid({
                    url : "${path}/feed/queryPage",
                    //editurl:"${path}/feed/change",   //指定增删改操作的url   添加oper=add  修改oper=edit  删除oper=del
                    datatype : "json",
                    mtype : "post",
                    rowNum:1,
                    rowList : [ 1,3,5,20, 30 ],
                    pager : '#feedpage',
                    viewrecords : true,  //是否展示总条数
                    caption : "反馈数据",
                    styleUI:"Bootstrap",
                    autowidth:true,
                    height:"auto",
                    //sortname : '',  //排序的字段
                   // sortorder : "desc",  //排序的方式
                    colNames : [ 'ID', '标题','内容', '时间', '用户ID','用户姓名'],
                    colModel : [
                        {name : 'id',width : 55},
                        {name : 'title',editable:true,width : 90},
                        {name : 'content',editable:true,width : 100},
                        {name : 'feedTime',editable:true,width : 100},
                        {name : 'userId',editable:true,width : 100},
                        {name : 'user.name',editable:true,width : 100},
                        /*{name : 'status',editable:true,width : 100},
                        {name : 'registTime',width : 80,align : "right"}*/
                        /*{name : 'deptId',editable:true,width : 150,edittype:"select",editoptions:{dataUrl:"${path}/dept/queryAll"},  //editoptions:{value:"男:男生;女:女孩"}
                            formatter:function(cellvalue, options, rowObject){
                                //console.log(rowObject);
                                return rowObject.depts.name;
                            }
                        }*/
                    ]
                }).navGrid("#feedpage", //开启增删改查操作  指定分页工具栏的位置
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
        </script>

        <%--面板--%>
        <div class="panel panel-danger">
            <%--面板头--%>
            <div class="panel panel-heading">反馈信息展示</div>

            <%--面板内容--%>
            <div class="panel panel-body">
                <%--初始化jqGrid--%>
                <table id="feedtab" />

                <%--分页工具栏--%>
                <div id="feedpage" />
            </div>

        </div>