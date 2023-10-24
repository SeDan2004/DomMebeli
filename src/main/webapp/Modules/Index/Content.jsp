<%@page language="Java" contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="./Static/Index/Content/Content.css">

<div class="showcase">
	<div class="showcase_header">
		<div class="showcase_header_row1">
			<div class="showcase_header_row1_box1">
				<div class="showcase_search">
					<%@include file="../../Icons/Лупа.svg" %>
					<input type="text" class="showcase_search_inp" placeholder="Поиск товара">
				</div>
			</div>
		
			<div class="showcase_header_row1_box2">
				<div class="showcase_product_type">
					<div class="product_type_header">
						<p>Типы товаров</p>
						<p>v</p>
					</div>
					
					<div class="product_type_content">
						<div class="product_type_list"></div>
					</div>
				</div>
					
				<div class="showcase_size">
					<div class="active_showcase_size">
						<p>20</p>
					</div>
				
					<div>
						<p>40</p>
					</div>
				
					<div>
						<p>60</p>
					</div>
				</div>
			</div>
		</div>
		
		<div class="showcase_header_row2">
			<div class="reset_filters">
				<p>Сброс фильтров</p>
			</div>
		
			<div class="showcase_cancel_btn">
				<%@include file="../../Icons/Отмена.svg" %>
			</div>
			
			<div class="showcase_accept_btn">
				<%@include file="../../Icons/Подтвердить.svg" %>
			</div>
		</div>
	</div>
	
	<div class="showcase_content" id="showcase">
		<p class="not_found_txt">Ничего не найдено!</p>
		<div v-for="product in products" class="product" :style="product.style" :id="product.id">
			<img :src="product.src">
			
			<div class="product_title">
				<span>{{product.price}}</span>
				<span>{{product.name}}</span>
			</div>
			
			<div class="product_btns">
				<div class="product_comments_btn">
					<p>Отзывы</p>
				</div>
				
				<div class="add_product_btn" v-on:click="addProductInBacket()">
					<p>В корзину</p>
					<%@include file="../../Icons/Корзина.svg" %>
				</div>
				
				<!--<div class="added">
					<p>В корзине</p>
					<%@include file="../../Icons/Корзина.svg" %>
				</div>-->
			</div>
		</div>
	</div>
	
	<div class="toggle_showcase_btn">
		<div class="prev_showcase_btn">
			<%@include file="../../Icons/Стрелка.svg" %>
		</div>
		
		<div class="next_showcase_btn">
			<%@include file="../../Icons/Стрелка.svg" %>
		</div>
	</div>
</div>

<script src="./Static/Index/Content/Content.js"></script>