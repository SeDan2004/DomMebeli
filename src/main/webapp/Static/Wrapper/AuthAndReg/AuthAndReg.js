authAndRegHeader = $(".auth_and_reg_header")[0];
[regBtn, authBtn] = authAndRegHeader.children;
[regForm, authForm, fpForm] = $(".auth_and_reg_content")[0].children;
[close, signIn, back, accept] = $(".auth_and_reg_footer_btns")[0].children;

fpBtn = $(".forgot_pass_p")[0];

function toggleForm() {
	
	if (this === authBtn) {
		authBtn.classList.remove("not_active");
		regBtn.classList.add("not_active");
		
		TweenMax.to(regForm, 0.5, {opacity: 0, onComplete: () => {
			regForm.style.display = "none";
		
			fpBtn.style.opacity = 1;
			fpBtn.style.cursor = "pointer";
			
			TweenMax.to(authForm, 0.5, {opacity: 1, onStart: () => {
				authForm.style.display = "flex";
			}})
			
			regBtn.addEventListener("click", toggleForm);
			fpBtn.addEventListener("click", toggleForm);
			
			authBtn.removeEventListener("click", toggleForm);
		}})
	}
	
	if (this === regBtn) {
		regBtn.classList.remove("not_active");
		authBtn.classList.add("not_active");
		
		TweenMax.to(authForm, 0.5, {opacity: 0, onComplete: () => {
			authForm.style.display = "none";
			
			TweenMax.to(regForm, 0.5, {opacity: 1, onStart: () => {
				regForm.style.display = "flex";
			}})
			
			authBtn.addEventListener("click", toggleForm);
			
			regBtn.removeEventListener("click", toggleForm);
			fpBtn.removeEventListener("click", toggleForm);
		}})
		
		fpBtn.style.opacity = 0;
		fpBtn.style.cursor = "default";
	}
	
	if (this === fpBtn) {
		authAndRegHeader.style.opacity = "50%";
		authAndRegHeader.style.pointerEvents = "none";
		
		close.style.display = "none";
		signIn.style.display = "none";
		
		back.style.display = "flex";
		accept.style.display = "flex";
		
		fpBtn.style.opacity = 0;
		fpBtn.style.cursor = "default";
						
		TweenMax.to(authForm, 0.5, {opacity: 0, onComplete: () => {
			authForm.style.display = "none";
						
			TweenMax.to(fpForm, 0.5, {opacity: 1, onStart: () => {
				fpForm.style.display = "flex";
			}})
		}})
	}
}

function toggleProfileAndSigninBtn() {
	let profileBtn = $(".profile_btn")[0],
		signInBtn = $(".signin_btn")[0];
		
	profileBtn.style.display = "flex";
	signInBtn.style.display = "none";
}

function closeAuthAndRegForm() {
	let wrapper = $(".wrapper")[0],
	    authAndRegForm = $(".auth_and_reg_form")[0],
	    inputs;
	
	if (getComputedStyle(regForm).display === "flex") {
		inputs = [...regForm.querySelectorAll("input")]
	}
	
	if (getComputedStyle(authForm).display === "flex") {
		inputs = [...authForm.querySelectorAll("input")];
	}
	
	inputs = inputs.filter(inp => inp.value.trim() !== "");
	
	i = 0;
	while (i < inputs.length) {
		
		if (inputs[i].type === "checkbox") {
			inputs[i].checked = false;
		} else {
			inputs[i].value = "";
		}
		
		i++
	}
	
	TweenMax.to(wrapper, 0.5, {opacity: 0, onComplete: () => {
		authAndRegForm.style.display = "none";
		wrapper.style.display = "none";
	}});
}

function checkEmail(email) {
	emailRegexp = /\w{0,}@\w{0,}.\w{0,}/;
	return !!email.match(emailRegexp);
}

function signInForm() {
	backetKey = localStorage.getItem("backetKey");
	
	if (backetKey === null) backetKey += "";
	
	function checkFields(inputs) {
		let fieldsLen = inputs.filter(inp => inp.type !== "checkbox")
		      				  .filter(inp => inp.value.trim() === "");
		      				  
		return fieldsLen < 1;
	}
	
	if (getComputedStyle(regForm).display === "flex") {
		let regFormInputs = [...regForm.querySelectorAll("input")];
				
		if (!checkFields(regFormInputs)) {
			alert("Есть пустые поля!");
			return;
		}
		
		[login, pass, email, repeatPass] = regFormInputs.map(inp => inp.value);
		
		if (!checkEmail(email)) {
			alert("Некорректно введена электронная почта!");
			return;
		}
		
		if (pass !== repeatPass) {
			alert("Пароли не совпадают!");
			return;
		}
		
		$.ajax({
			method: "POST",
			url: "./AuthAndReg",
			data: {
				type: "reg",
				backetKey: backetKey,
				login: login,
				pass: pass,
				email: email
			},
			success(arg) {
				
				if (arg === "") {
					closeAuthAndRegForm();
					toggleProfileAndSigninBtn();
				} else {
					alert(arg);
					return;
				}
			}
		})
	}
	    
	if (getComputedStyle(authForm).display === "flex") {
		let authFormInputs = [...authForm.querySelectorAll("input")];
		
		function getValue(inp) {
			return inp.type === "checkbox" ? inp.checked + "" : inp.value;
		}
		
		if (!checkFields(authFormInputs)) {
			alert("Есть пустые поля!");
			return;
		}
		
		[login, pass, rememberMe] = authFormInputs.map(getValue);
				
		$.ajax({
			method: "POST",
			url: "./AuthAndReg",
			data: {
				type: "auth",
				backetKey: backetKey,
				login: login,
				pass: pass,
				rememberMe: rememberMe
			},
			success(arg) {				
				if (arg === "") {
					closeAuthAndRegForm();
					toggleProfileAndSigninBtn();
				} else {
					alert(arg);
					return;
				}
			}
		})
	}
}

function forgotPassEvents() {
	let fpInputs = [...fpForm.querySelectorAll("input")],
	    fpBoxes = fpInputs.map(inp => inp.parentNode),
	    notDisabledInputs = fpInputs.filter((inp) => {
			return getComputedStyle(inp.parentNode).opacity !== "0.5";
		})
	
	function toAuthForm() {
		back.style.display = "none";
		accept.style.display = "none";
			
		close.style.display = "flex";
		signIn.style.display = "flex";
			
		TweenMax.to(fpForm, 0.5, {opacity: 0, onComplete: () => {
			fpForm.style.display = "none";
				
			fpBtn.style.opacity = 1;
			fpBtn.style.cursor = "pointer";
				
			authAndRegHeader.style.opacity = 1;
			authAndRegHeader.style.pointerEvents = "auto";
				
			TweenMax.to(authForm, 0.5, {opacity: 1, onStart: () => {
				authForm.style.display = "flex";
			}});
		}})
	}
	
	if (this === back) {
		
		if (notDisabledInputs.length === 1) {
			
			if (notDisabledInputs[0].value.trim() !== "") {
				notDisabledInputs[0].value = "";
			}
			
			if (notDisabledInputs[0].classList[1] === "fp_email_inp") {
				toAuthForm();
			}
			
			if (notDisabledInputs[0].classList[1] === "fp_token_inp") {
				fpBoxes[2].style.opacity = "50%";
				fpBoxes[2].style.pointerEvents = "none";
				
				fpBoxes[0].style.opacity = 1;
				fpBoxes[0].style.pointerEvents = "auto";
			}
		}
		
		if (notDisabledInputs.length === 2) {
			notDisabledInputs[0].value = "";
			notDisabledInputs[1].value = "";
			
			fpBoxes[1].style.opacity = "50%";
			fpBoxes[1].style.pointerEvents = "none";
			
			fpBoxes[3].style.opacity = "50%";
			fpBoxes[3].style.pointerEvents = "none";
			
			fpBoxes[2].style.opacity = 1;
			fpBoxes[2].style.pointerEvents = "auto";
		}
	}
	
	if (this === accept) {
				
		if (notDisabledInputs.length === 1) {
					
			if (notDisabledInputs[0].classList[1] === "fp_email_inp") {
				email = notDisabledInputs[0].value.trim();
				
				if (email === "") {
					alert("Есть незаполненные поля!");
					return;
				}
				
				if (!checkEmail(email)) {
					alert("Некорректно введена электронная почта!");
					return;
				}
				
				$.ajax({
					method: "POST",
					url: "./ForgotPass",
					data: {email: email},
					success(arg) {
						
						if (arg !== "") {
							alert(arg);
							return;
						} else {
							fpBoxes[2].style.opacity = 1;
							fpBoxes[2].style.pointerEvents = "auto";
							
							fpBoxes[0].style.opacity = "50%";
							fpBoxes[0].style.pointerEvents = "none";
						}
					}
				})
			}
			
			if (notDisabledInputs[0].classList[1] === "fp_token_inp") {
				token = notDisabledInputs[0].value.trim();
				
				if (token === "") {
					alert("Есть незаполненные поля!");
					return;
				}
				
				$.ajax({
					method: "POST",
					url: "./ForgotPass",
					data: {token: token},
					success(arg) {
						
						if (arg !== "") {
							alert(arg);
							return;
						} else {
							fpBoxes[2].style.opacity = "50%";
							fpBoxes[2].style.pointerEvents = "none";
							
							fpBoxes[1].style.opacity = 1;
							fpBoxes[1].style.pointerEvents = "auto";
							
							fpBoxes[3].style.opacity = 1;
							fpBoxes[3].style.pointerEvents = "auto";
						}
					}
				})
			}
		}
		
		if (notDisabledInputs.length === 2) {
			[pass, repeatPass] = notDisabledInputs.map(inp => inp.value.trim());
			email = fpInputs[0].value.trim();
			
			if (pass === "" || repeatPass === "" || email === "") {
				alert("Есть незаполненные поля!");
				return;
			}
			
			if (pass !== repeatPass) {
				alert("Пароли не совпадают!");
				return;
			}
			
			$.ajax({
				method: "POST",
				url: "./ForgotPass",
				data: {
					pass: pass,
					email: email
				},
				success(arg) {
					
					if (arg !== "") {
						toAuthForm();
						
						for (i = 0; i < fpBoxes.length; i++) {
							fpInputs[i].value = "";
							
							if (i < 1) {
								fpBoxes[i].style.opacity = 1;
								fpBoxes[i].style.pointerEvents = "auto";
							} else {
								if (getComputedStyle(fpBoxes[i]).opacity !== "0.5") {
									fpBoxes[i].style.opacity = "50%";
									fpBoxes[i].style.pointerEvents = "none";
								}
							}
						}
					}
				}
			})
		}
	}
}

function addEvent() {
	authBtn.addEventListener("click", toggleForm);
	fpBtn.addEventListener("click", toggleForm);
	
	close.addEventListener("click", closeAuthAndRegForm);
	signIn.addEventListener("click", signInForm);
	
	back.addEventListener("click", forgotPassEvents);
	accept.addEventListener("click", forgotPassEvents);
}

addEvent();