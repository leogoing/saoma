function showCallBackMessage(json , success , fail){
	if(json.result == true){
		$.messager.alert("信息" , json.message , "info" , function(){
			success();//success是一个函数类型
		});
	}else{
		$.messager.alert("错误" , json.message , "error" , function(){
			if(fail != null){
				fail();//函数名()代表了执行该函数
			}
		});
	}
}