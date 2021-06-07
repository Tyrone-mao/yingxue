<%@page isELIgnored="false" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

        <script>

            $(function(){
                pageInit();
            });
            function pageInit(){
                jQuery("#catetab").jqGrid(
                    {
                        url :"${path}/category/queryByOne",
                        editurl:"${path}/category/adit",
                        datatype : "json",
                        rowNum : 2,
                        rowList : [2, 4, 10, 20, 30 ],
                        pager : '#catepage',
                        sortname : 'id',
                        viewrecords : true,
                        sortorder : "desc",
                        multiselect : false,
                        subGrid : true,
                        caption : "一级类别",
                        styleUI:"Bootstrap",
                        autowidth:true,
                        height:"auto",
                        colNames : [ 'ID', '类名', '级别', '父类ID' ],
                        colModel : [
                            {name : 'id', width : 55},
                            {name : 'cateName',editable:true,width : 90},
                            {name : 'levels',width : 100},
                            {name : 'parentId',width : 80,align : "right",},

                        ],

                        subGridRowExpanded : function(subgrid_id, row_id) {
                            addSubGrid(subgrid_id,row_id);
                        }

                    });
                jQuery("#catetab").jqGrid('navGrid', '#catepage', { edit:true,edittext:"修改",del:true,deltext:"删除",add:true,addtext:"添加",search:true,
                },
                    {
                        closeAfterEdit:true
                    },
                    {
                        closeAfterAdd:close,
                    },
                    {
                        closeAfterDel: true,
                        afterSubmit:function (data) {
                            alert(data.responseJSON.massge)
                            return"123";
                        }
                    });
            }

            function addSubGrid(subgrid_id,row_id) {
                // we pass two parameters
                // subgrid_id is a id of the div tag created whitin a table data
                // the id of this elemenet is a combination of the "sg_" + id of the row
                // the row_id is the id of the row
                // If we wan to pass additinal parameters to the url we can use
                // a method getRowData(row_id) - which returns associative array in type name-value
                // here we can easy construct the flowing

                /*var subgrid_table_id, pager_id;

                subgrid_table_id = subgrid_id + "_t";

                pager_id = "p_" + subgrid_table_id;*/

                var tableid = subgrid_id + "_t";
                var pageid="p_" + subgrid_id;

                $("#" + subgrid_id).html(
                    "<table id='" + tableid + "' class='scroll'></table><div id='" + pageid + "' class='scroll'></div>");

                jQuery("#" + tableid).jqGrid(
                    {
                        url :"${path}/category/queryByTwo?id="+row_id,
                        editurl:"${path}/category/adit?parentId="+row_id,
                        datatype : "json",
                        rowNum : 4,
                        rowList : [2,4,8, 10, 20, 30 ],
                        pager : '#'+pageid,
                        sortname : 'id',
                        viewrecords : true,
                        sortorder : "desc",
                        multiselect : false,
                        subGrid : true,
                        styleUI:"Bootstrap",
                        autowidth:true,
                        height:"auto",
                       // caption : "Grid as Subgrid",
                        colNames : [ 'ID', '类名', '级别', '父类ID' ],
                        colModel : [
                            {name : 'id', width : 55},
                            {name : 'cateName',editable:true,width : 90},
                            {name : 'levels',width : 100},
                            {name : 'parentId',width : 80,align : "right"},

                        ],
                    });
                jQuery("#" + tableid).jqGrid('navGrid',
                    "#" + pageid, {edit:true,edittext:"修改",del:true,deltext:"删除",add:true,addtext:"添加",search:true
                    },
                    {
                        closeAfterEdit:true
                    },
                    {
                        closeAfterAdd:close,
                    },
                    {
                        closeAfterDel: true,
                        afterSubmit:function (data) {
                            alert(data.responseJSON.massge)
                            return"123";
                        }
                    });

                };

        </script>

        <%--面板--%>
        <div class="panel panel-danger">
            <%--面板头--%>
            <div class="panel panel-heading">用户信息展示</div>

            <%--面板内容--%>
            <div class="panel panel-body">
                <%--初始化jqGrid--%>
                <table id="catetab" />

                <%--分页工具栏--%>
                <div id="catepage" />
            </div>

        </div>