
productTypeBtn = $(".product_type_header")[0];
searchInp = $(".showcase_search_inp")[0];
showcaseSizeChild = $(".showcase_size")[0].children;
filters = {search: "", productType: -1, showcaseSize: 20};
offsetBtnIndex = [];
currentBtnIndex = 0;

acceptFilter = $(".showcase_accept_btn")[0];
cancelFilter = $(".showcase_cancel_btn")[0];
resetFilters = $(".reset_filters")[0];

prevShowcaseBtn = $(".prev_showcase_btn")[0];
nextShowcaseBtn = $(".next_showcase_btn")[0];

showcase = new Vue({
	el: "#showcase",
	data: {
		products: []
	},
	methods: {
		addProductInBacket: function () {
			let addBtn = event.currentTarget,
			    backetKey = localStorage.getItem("backetKey"),
			    productId = addBtn.parentNode.parentNode.id;
			
			addBtn.classList.remove("add_product_btn");
			addBtn.classList.add("added");
			
			addBtn.children[0].innerText = "В корзине";
			
			this.$off("click", this.addProductInBacket);
			
			if (backetKey === null) backetKey += "";
			
			$.ajax({
				method: "POST",
				url: "./AddProduct",
				data: {
					backetKey: backetKey,
					productId: productId
				},
				success(arg) {
					console.log(arg);
					
					if (arg !== "") {
						if (arg.length === 8) {
							localStorage.setItem("backetKey", arg);
						}
					}
				}
			})
		}
	}
})

function getProductTypes() {
	$.ajax({
		method: "POST",
		url: "./GetProductTypes",
		success(productTypes) {
			productTypes = JSON.parse(productTypes);
			productTypeList = $(".product_type_list")[0];
			
			for (i = 0; i < productTypes.length; i++) {
				div = document.createElement("div");
				p = document.createElement("p");
				
				p.innerText = productTypes[i];
				
				div.id = "productType" + (i + 1);
				div.classList.add("product_type_list_el");
				div.addEventListener("click", openAndCloseFilterBtns);
				
				div.appendChild(p);
				productTypeList.appendChild(div);
			}
		}
	})
}

function openAndCloseProductTypeBtn() {
	let productTypeHeader = $(".product_type_header")[0],
		productTypeList = $(".product_type_list")[0];
	
	if (getComputedStyle(productTypeList).top !== "0px") {
		TweenMax.to(productTypeList, 1, {top: "0vh"});
		productTypeHeader.children[1].style.transform = "rotate(180deg)";
	} else {
		TweenMax.to(productTypeList, 1, {top: "-25vh"});
		productTypeHeader.children[1].style.transform = "";
	}
}

function openAndCloseFilterBtns() {
	let showcaseHeader = $(".showcase_header")[0],
	    headerHeight = getComputedStyle(showcaseHeader).height,
	    productTypeActive = $(".product_type_active")[0],
	    activeShowcaseSize = $(".active_showcase_size")[0];
	    	
	function openFilterBtns() {
		showcaseHeader.style.height = "15vh";
		
		cancelFilter.style.display = "flex";
		acceptFilter.style.display = "flex";
	}
	
	function closeFilterBtns() {
		showcaseHeader.style.height = "6vh";
		
		cancelFilter.style.display = "none";
		acceptFilter.style.display = "none";
	}
	
	function clearFilters() {
		if (filters.productType !== -1) {
			productTypeActive.classList.add("product_type_list_el");
			productTypeActive.classList.remove("product_type_active");
		
			productTypeActive.addEventListener("click", openAndCloseFilterBtns);
			
			filters.productType = -1;
		}
		
		if (filters.showcaseSize !== 20) {
			activeShowcaseSize.classList.remove("active_showcase_size");
			activeShowcaseSize.addEventListener("click", openAndCloseFilterBtns);
			
			$(".showcase_size div").first()[0].classList.add("active_showcase_size");
			
			filters.showcaseSize = 20;
		}
	}
	
	headerHeight = Math.trunc(parseFloat(headerHeight));
		
	if (this === searchInp) {
		let key = event.key;
		
		if (key === "Backspace") {	
			if (searchInp.value.length === 0) {
				if (headerHeight > 100) closeFilterBtns();
			}
		} else {
			if (searchInp.value.length > 0) {
				if (headerHeight < 100) {
					openFilterBtns();	
				}
				
				clearFilters();
			}
		}
	}
	
	if (this === cancelFilter) {
		if (searchInp.value !== "") {
			searchInp.value = "";
			filters.search = "";
		}
		
		clearFilters();
		closeFilterBtns();
	} else {
		if (this.nodeName === "DIV") {
			if (headerHeight < 100) openFilterBtns();
			if (searchInp.value !== "") searchInp.value = "";
					
			if (this.classList.contains("product_type_list_el")) {			
				if (productTypeActive !== undefined) {
					productTypeActive.classList.remove("product_type_active");
					productTypeActive.classList.add("product_type_list_el");
					
					productTypeActive.addEventListener("click", openAndCloseFilterBtns);
				}
				
				filters.productType = +this.id.slice(this.id.indexOf("e") + 1);
				
				this.classList.remove("product_type_list_el");
				this.classList.add("product_type_active");
				
				this.removeEventListener("click", openAndCloseFilterBtns);
			}
			
			if (this.parentNode.classList.contains("showcase_size")) {		
				filters.showcaseSize = +this.innerText;
								
				this.classList.add("active_showcase_size");
				this.removeEventListener("click", openAndCloseFilterBtns);
				
				activeShowcaseSize.classList.remove("active_showcase_size");
				activeShowcaseSize.addEventListener("click", openAndCloseFilterBtns);
			}
		}
	}
	
}

function acceptFilterFunc() {
	let productTypeList = $(".product_type_list")[0],
	    showcaseSize = $(".showcase_size")[0];
	
	if (searchInp.value !== "") {
		filters.search = searchInp.value;
	}	
	
	searchInp.parentNode.style.opacity = "60%";
	searchInp.parentNode.style.pointerEvents = "none";
	
	productTypeList.style.pointerEvents = "none";
	showcaseSize.style.pointerEvents = "none";
	
	showcase.products.length = 0;
	
	resetFilters.style.display = "flex";
	
	acceptFilter.style.display = "none";
	cancelFilter.style.display = "none";
		
	getProductsOffset();
}

function getProductsOffset() {
	$.ajax({
		method: "POST",
		url: "./GetOffset",
		data: {filters: JSON.stringify(filters)},
		success(btnIndexes) {
			offsetBtnIndex = JSON.parse(btnIndexes);
						
			function checkShowcaseBtns() {
				if (getComputedStyle(prevShowcaseBtn).opacity !== "0.5") {
					prevShowcaseBtn.style.opacity = "0.5";
					prevShowcaseBtn.style.pointerEvents = "none";
				}
				
				if (getComputedStyle(nextShowcaseBtn).opacity !== "0.5") {
					nextShowcaseBtn.style.opacity = "0.5";
					nextShowcaseBtn.style.pointerEvents = "none";
				}
				
				currentBtnIndex = 0;
			}
			
			checkShowcaseBtns();
			
			if (offsetBtnIndex.length > 1) {
				nextShowcaseBtn.style.opacity = 1;
				nextShowcaseBtn.style.pointerEvents = "auto";
				
				nextShowcaseBtn.addEventListener("click", toggleShowcaseProducts);
			}
			
			getProductsInShowcase();
		}
	})
}

function toggleShowcaseProducts() {
	if (this === prevShowcaseBtn) {
		currentBtnIndex--;
		
		if (currentBtnIndex === 0) {
			prevShowcaseBtn.style.opacity = "50%";
			prevShowcaseBtn.style.pointerEvents = "none";
			
			prevShowcaseBtn.removeEventListener("click", toggleShowcaseProducts);
		}
		
		if (currentBtnIndex === offsetBtnIndex.length - 2) {
			nextShowcaseBtn.style.opacity = 1;
			nextShowcaseBtn.style.pointerEvents = "auto";
			
			nextShowcaseBtn.addEventListener("click", toggleShowcaseProducts);
		}
	}
	
	if (this === nextShowcaseBtn) {
		currentBtnIndex++;
		
		if (currentBtnIndex === 1) {
			prevShowcaseBtn.style.opacity = 1;
			prevShowcaseBtn.style.pointerEvents = "auto";
			
			prevShowcaseBtn.addEventListener("click", toggleShowcaseProducts);
		}
		
		if (currentBtnIndex === offsetBtnIndex.length - 1) {
			nextShowcaseBtn.style.opacity = "50%";
			nextShowcaseBtn.style.pointerEvents = "none";
			
			nextShowcaseBtn.removeEventListener("click", toggleShowcaseProducts);
		}
	}
	
	showcase.products.length = 0;
	
	getProductsInShowcase(currentBtnIndex);
}

function getProductsInShowcase(offset = 0) {
	function setShowcaseGridTemplateAreas(productRowLen = 4) {
		let rows = filters.showcaseSize / productRowLen,
			productRow = "";
			gridStr = "";
		
		if (rows % 1 !== 0) rows = Math.ceil(rows);
		
		productRow = ".".repeat(productRowLen).split("")
		                                      .join(" ");
		
		for (i = 1; i <= rows; i++) {
			gridStr += "\"" + productRow + "\"";
			if (i !== rows) gridStr += "\n";
		}
				
		return gridStr;
	}
	
	gridTemplateAreas = setShowcaseGridTemplateAreas();
		
	$.ajax({
		method: "POST",
		url: "./GetProducts",
		data: {
			offset: offset,
			filters: JSON.stringify(filters),
			backetKey: localStorage.getItem("backetKey") + ""
		},
		success(products) {
			products = JSON.parse(products);
						
			if (products.length > 0) {
				for (i = 0; i < products.length; i++) {				
					[id, name, price, imgSrc, inActiveBacket] = products[i];
				
					price = +price;
					style = "gridArea: product" + (i + 1);
			
					gridStr = gridStr.replace(".", "product" + (i + 1));
				
					product = {
						id: id,
						name: name,
						price: price.toLocaleString() + " руб",
						src: "./" + imgSrc,
						//inBacket: inBacket,
						style: style,
					}
				
					showcase.products.push(product);
				}
			
				showcase.$el.style.gridTemplateAreas = gridStr;	
			} else {
				let productsLen = showcase.products.length;
				
				showcase.$el.style.gridTemplateAreas = "";
				showcase.products.splice(0, productsLen);
				
				showcase.$el.children[0].style.display = "block";
			}
		}
	})
}

function resetFiltersFunc() {
	let productTypeList = $(".product_type_list")[0],
	    showcaseSize = $(".showcase_size")[0];
	
	if (showcase.products.length === 0) {
		showcase.$el.children[0].style.display = "none";
	}
	
	searchInp.parentNode.style.opacity = 1;
	searchInp.parentNode.style.pointerEvents = "auto";
	
	productTypeList.style.pointerEvents = "auto";
	showcaseSize.style.pointerEvents = "auto";
	
	resetFilters.style.display = "none";
	
	cancelFilter.click();
	
	showcase.products.length = 0;
	getProductsOffset();
}

function addEvents() {
	productTypeBtn.addEventListener("click", openAndCloseProductTypeBtn);
	
	searchInp.addEventListener("keyup", openAndCloseFilterBtns);
	
	for (size of showcaseSizeChild) {
		if (!size.classList.contains("active_showcase_size")) {
			size.addEventListener("click", openAndCloseFilterBtns);
		}
	}
	
	acceptFilter.addEventListener("click", acceptFilterFunc);
	cancelFilter.addEventListener("click", openAndCloseFilterBtns);
	resetFilters.addEventListener("click", resetFiltersFunc);
}

getProductsOffset();
getProductTypes();

addEvents();