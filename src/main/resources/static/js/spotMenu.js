function spotCategorySearch(e) {
	var category = e.getAttribute("id");
	/*var dataValue = $("#dataValue").val();*/
	var form = document.createElement("form");
	var parm = new Array();
	var input = new Array();
	
	form.action = "/spots/" + category + "/search";
	form.method = "POST";
	
	/*parm.push(['dataValue', dataValue]);*/
	
	for(var i=0; i<parm.length; i++) {
		input[i] = document.createElement("input");
		input[i].setAttribute("type", "hidden");
		input[i].setAttribute("name", parm[i][0]);
		input[i].setAttribute("value", parm[i][1]);
		
		form.appendChild(input[i]);
	}
	
	document.body.appendChild(form);
	form.submit();
}