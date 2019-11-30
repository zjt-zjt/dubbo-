var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8084/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",

			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					//var html = username + "，欢迎来到淘淘！<a href=\"http://localhost:8084/user/logout\" class=\"link-logout\">[退出]</a>";
					var html = username + "，欢迎来到淘淘！<a href='javascript:0' onclick='TT.logOut()'  class=\"link-logout\" >[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	},


	logOut : function () {
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8084/user/logout/" + _ticket,
			dataType : "json",
			type : "Get",
			xhrFields:{withCredentials:true},
			success : function (data) {
				if (data.status == 200) {
					window.location.href = "http://localhost:8082";
					console.log("退出成功");
				}
			}
		});
	}

}



$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});

// logout : function() {
// 	var _ticket = $.cookie("TT_TOKEN");
// 	if (!_ticket) {
// 		return;
// 	}
// 	$.ajax({
// 		url : "http://localhost:8084/user/logout/" + _ticket,
// 		dataType : "jsonp",
// 		type : "GET",
// 		success : function(data){
// 			if(data.status == 200){
//              window.location.href="http://localhost:8082";
// 			}
// 		}
// 	});
//
//
// }