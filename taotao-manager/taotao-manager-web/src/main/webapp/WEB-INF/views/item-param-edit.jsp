<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table cellpadding="5" style="margin-left: 30px" id="itemParamEditTable" class="itemParam">
	<tr>
		<td>商品类目:</td>
		<td>
			<input id="itemCatId" name="cid" type="hidden" />
			<span id="itemCat"></span>
		</td>
	</tr>
	<tr class="addGroupTr">
		<td>规格参数:</td>
		<td>
			<ul id="ItemParamUL">
				<li><a href="javascript:void(0)" class="easyui-linkbutton addGroup">添加分组</a></li>
			</ul>
		</td>
	</tr>

	<tr>
		<td></td>
		<td>
			<a href="javascript:void(0)" class="easyui-linkbutton submit">提交</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton close">关闭</a>
		</td>
	</tr>
</table>
<div class="itemParamAddTemplate" style="display: none;">
	<li class="param">
		<ul>
			<li>
				<input class="easyui-textbox" style="width: 150px;" name="group"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton addParam"  title="添加参数" data-options="plain:true,iconCls:'icon-add'"></a>
			</li>
			<li>
				<span>|-------</span><input  style="width: 150px;" class="easyui-textbox" name="param"/>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton delParam" title="删除" data-options="plain:true,iconCls:'icon-cancel'"></a>
			</li>
		</ul>
	</li>
</div>

<span id="data_span" style="display: none" />
<script text="text/javascript">
	$(function(){

		delayLoadData();

		
		$(".addGroup").click(function(){
			  var temple = $(".itemParamAddTemplate li").eq(0).clone();
			  $(this).parent().parent().append(temple);
			  temple.find(".addParam").click(function(){
				  var li = $(".itemParamAddTemplate li").eq(2).clone();
				  li.find(".delParam").click(function(){
					  $(this).parent().remove();
				  });
				  li.appendTo($(this).parentsUntil("ul").parent());
			  });
			  temple.find(".delParam").click(function(){
				  $(this).parent().remove();
			  });
		 });
		
		$("#itemParamEditTable .close").click(function(){
			$(".panel-tool-close").click();
		});
		
		$("#itemParamEditTable .submit").click(function(){
			var params = [];
			// 循环获取动态生成的模板表单中，用户输入的内容， 动态构建后台需要的模板JSON数据
			var groups = $("#itemParamEditTable [name=group]");
			groups.each(function(i,e){
				var p = $(e).parentsUntil("ul").parent().find("[name=param]");
				// 拼装每个规格模板分组下项
				var _ps = [];
				p.each(function(_i,_e){
					var _val = $(_e).siblings("input").val();
					if($.trim(_val).length>0){
						_ps.push(_val);						
					}
				});
				var _val = $(e).siblings("input").val();
				if($.trim(_val).length>0 && _ps.length > 0){
					// 拼装所有分组
					// [{"group":"主体参数", "params": ["机身颜色","产品型号","上市年份"]}]
				    params.push({
						"group":_val,
						"params":_ps
					});					
				}
			});
			// 请求后台接口， 修改规格模板数据
			var url = "/item/param/update/"+$("#itemParamEditTable [name=cid]").val();
			$.post(url,{"paramData":JSON.stringify(params)},function(data){
				if(data.status == 200){
					$.messager.alert('提示','修改商品规格成功!',undefined,function(){
						$(".panel-tool-close").click();
    					$("#itemParamList").datagrid("reload");
    				});
				}
			});
		});
	});

	/**
	 *  采用延时的方式，回显item-param-list页面通过span标签传过来的数据
	 */
	function delayLoadData() {
		setTimeout(function(){
			var data = JSON.parse($("#data_span").attr("item-param-data"));
			// console.log(data);
			$("#itemCatId").val(data.itemCatId);
			$("#itemCat").text(data.itemCatName);
			// console.log(data.paramData);
			var itemParamData = JSON.parse(data.paramData);


			$.each(itemParamData, function(idx, itemGroupObj) {
				var groupName = itemGroupObj.group;


				var temple = $(".itemParamAddTemplate li").eq(0).clone();
				$("#ItemParamUL").append(temple);

				temple.find("input[name='group']").prev().val(groupName);

				temple.find("ul li").last().remove();

				var groupItems = itemGroupObj.params;
				$.each(groupItems, function(idx, item){
					// console.log("组内的Key: " + item);
					var li = $(".itemParamAddTemplate li").eq(2).clone();
					li.find(".delParam").click(function(){
						$(this).parent().remove();
					});
					li.appendTo(temple.find("ul"));

					li.find("input[name='param']").prev().val(item);
				});

				temple.find(".addParam").click(function(){
					var li = $(".itemParamAddTemplate li").eq(2).clone();
					li.find(".delParam").click(function(){
						$(this).parent().remove();
					});
					li.appendTo($(this).parentsUntil("ul").parent());
				});
				temple.find(".delParam").click(function(){
					$(this).parent().remove();
				});
			});

		}, 500);
	}
</script>