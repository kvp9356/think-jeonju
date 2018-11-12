document.getElementById($('#category').val()).style.color = "black";
pagingBar($("#cPage").val());
chkCurrentPage();
var url = "/spots/"+$("#category").val()+"/search";

function pagingBar(cPage) {
	var size = $("#size").val();
	var html = "<li class='page-item'>";
	html += "<a class='page-link' href='#' aria-label='Previous' onclick='pre()'>";
	html += "<span aria-hidden='true'>&laquo;</span>";
	html += "<span class='sr-only'>Previous</span></a></li>";
	
	var from = 1;
	if(parseInt(cPage%5)==0) {
		from = (parseInt(cPage/5)-1)*5+1;
	} else {
		from = parseInt(cPage/5)*5+1;
	}
	var end = from + 5;
	
	if(end > size) {
		end = parseInt(size) + 1;
	}
	
	for(var i=from; i<end; i++) {
		html += "<li class='page-item'><a class='page-link' onclick='getPage(this);'>"+i+"</a></li>";	
	}

	html += "<li class='page-item' onclick='next()'>";
	html += "<a class='page-link' href='#' aria-label='Next'>";
	html += "<span aria-hidden='true'>&raquo;</span>";
	html += "<span class='sr-only'>Next</span></a></li>";
	$(".pagination").html(html);
	chkCurrentPage();
}

function pre() {
	var cPage = $("#cPage").val();
	var startPage = 1;
	
	if(parseInt(cPage%5)==0) {
		startPage = (parseInt(cPage/5)-1)*5+1;
	} else {
		startPage = parseInt(cPage/5)*5+1;
	}
	
	if(startPage != 1) {
		pagingBar(startPage-5);
	}
	
	if(startPage > 5)
		moveToPage(url, startPage-5);
}

function next() {
	var cPage = $("#cPage").val();
	var startPage = 1;
	var size = $("#size").val();
	
	if(parseInt(cPage%5)==0) {
		startPage = (parseInt(cPage/5)-1)*5+1;
	} else {
		startPage = parseInt(cPage/5)*5+1;
	}
	
	if(startPage+5 < size) {
		pagingBar(startPage+5);
		moveToPage(url, startPage+5);
	}	
}

function getPage(a) {
	var cPage = a.innerHTML;
	moveToPage(url, cPage);
}

function chkCurrentPage() {
	var cPage = $("#cPage").val();
	var pages = document.getElementsByClassName("page-link");
	for (var i = 0; i < pages.length; i++) {
		if(pages[i].innerHTML == cPage) {
			pages[i].style.backgroundColor = "#007bff";
			pages[i].style.color = "white";
			return;
		}
	}
}

function moveToPage(url, cPage) {
	var dataValue = $("#dataValue").val();
	var form = document.createElement("form");
	var parm = new Array();
	var input = new Array();

	form.action = url;
	form.method = "POST";
	
	parm.push(['dataValue', dataValue]);
	parm.push(['cPage', cPage]);
	
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