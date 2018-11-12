function spotCategorySearch(e) {
	var a = document.getElementsByClassName("searchMenu");
	var category = "All";
	
	if(e.tagName == "A") {
		category = e.getAttribute("id");
	} else {
		for(var i=0; i<a.length; i++) {
			if(a[i].style.color == "black") {
				category = a[i].getAttribute("id");
			}
		}
	}
	
	var form = document.createElement("form");
	var parm = new Array();
	var input = new Array();
	
	form.action = "/spots/" + category + "/search";
	form.method = "POST";

	if(e.tagName == "BUTTON") {
		parm.push(['dataValue', $("#searchKeyword").val()]);
	}
		
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