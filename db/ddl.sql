
select setval('category_seq', 1, false);
select setval('prod_seq', 1, false);
select setval('option_seq', 1, false);


-- [시퀀스 생성] : user_no 시퀀스
CREATE SEQUENCE user_seq START 1 INCREMENT 1;

/* USER 테이블 생성 시작 */

-- [시퀀스 생성] : user_no 시퀀스
CREATE SEQUENCE user_seq START 1 INCREMENT 1;

-- [테이블 생성] : users
CREATE TABLE users (
    user_no BIGINT PRIMARY KEY DEFAULT nextval('user_seq'),  -- 기본키, 시퀀스 사용

    userid VARCHAR(20) NOT NULL UNIQUE,        -- 아이디 (중복 X)
    password VARCHAR(255) NOT NULL,            -- 비밀번호
    name VARCHAR(255) NOT NULL,                -- 이름

    birthday DATE,                             -- 생년월일

    tel VARCHAR(20),                           -- 전화번호
    email VARCHAR(255) NOT NULL UNIQUE,        -- 이메일 (중복 X)

    -- [주소] : Address 클래스의 @Embeddable 구성
    zipcode VARCHAR(20),                       -- 우편번호
    address VARCHAR(255),                      -- 주소
    detail_address VARCHAR(255),               -- 상세주소

    point INTEGER DEFAULT 0,                   -- 포인트 (기본값 0)

    registerday TIMESTAMP DEFAULT CURRENT_TIMESTAMP,        -- 가입일자
    passwdupdateday TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 비밀번호 변경일자

    status VARCHAR(20),                        -- 회원 상태 (ENUM: ACTIVE, IDLE, DELETED)
    role VARCHAR(20)                           -- 권한 (ENUM: USER, ADMIN)
);


-- [제약조건]
ALTER TABLE users
    ADD CONSTRAINT CK_users_point_valid
        CHECK (point >= 0);

-- status 컬럼: 열거된 상태만 허용
ALTER TABLE users
    ADD CONSTRAINT CK_users_status_valid
        CHECK (status IN ('ACTIVE', 'IDLE', 'DELETED'));

-- role 컬럼: 'USER' 또는 'ADMIN'만 허용
ALTER TABLE users
    ADD CONSTRAINT CK_users_role_valid
        CHECK (role IN ('USER', 'ADMIN'));

/* USER 테이블 생성 끝 */


--------------------------------------------------------------------------------

/* LOGIN_HISTORY 테이블 생성 시작 */


-- 시퀀스 먼저 생성 (id에 사용할 시퀀스)
CREATE SEQUENCE login_seq START 1 INCREMENT 1;


-- 테이블 생성
CREATE TABLE login (
    historyno BIGINT PRIMARY KEY DEFAULT nextval('login_seq'),        -- 로그인 기록 번호, PK, 시퀀스 사용
    fk_user_no BIGINT NOT NULL,                                       -- 외래키: users.user_no
    clientip VARCHAR(255),                                            -- 로그인 시 IP 주소
    loginday TIMESTAMP DEFAULT CURRENT_TIMESTAMP                      -- 로그인 일시
);

-- 외래 키 제약 조건 추가
ALTER TABLE login
    ADD CONSTRAINT FK_login_fk_user_no
        FOREIGN KEY (fk_user_no) REFERENCES users(user_no)
            ON DELETE CASCADE;

CREATE INDEX IDX_login_fk_user_no ON login(fk_user_no);

/* LOGIN_HISTORY 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* CATEGORY 테이블 생성 시작 */


-- 1. 시퀀스 생성 (상품 번호용)
CREATE SEQUENCE category_seq START 1 INCREMENT 1;

-- 2. 테이블 생성
CREATE TABLE category (
    categoryno BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),     -- 카테고리 번호 (PK, 시퀀스 기반)
    major_category VARCHAR(255) NOT NULL,               -- 상품 대분류
    small_category VARCHAR(255) NOT NULL,               -- 상품 소분류
    category_img   VARCHAR(255) NOT NULL                -- 대표 이미지
);

/* CATEGORY 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* PRODUCTS 테이블 생성 시작 */

-- 1. 시퀀스 생성 (상품 번호용)
CREATE SEQUENCE prod_seq START 1 INCREMENT 1;

-- 2. 테이블 생성
CREATE TABLE products (
    productno BIGINT PRIMARY KEY DEFAULT nextval('prod_seq'),     -- 상품 번호 (PK, 시퀀스 기반)
    fk_categoryno BIGINT NOT NULL,            -- 카테고리 번호
    name VARCHAR(255) NOT NULL,               -- 상품명
    description VARCHAR(4000) NOT NULL ,      -- 상품설명
    price INTEGER NOT NULL,                   -- 가격
    thumbnail_img VARCHAR(255) NOT NULL,      -- 상품 미리보기 이미지
    registerday TIMESTAMP NOT NULL ,          -- 등록일자
    cnt     INTEGER NOT NULL ,                -- 재고
    delivery_price INTEGER NOT NULL ,         -- 배송비
    detail_html VARCHAR(255) NOT NULL,        -- 상품설명 html 경로
    point_pct INTEGER NOT NULL,               -- 포인트 적립비율 1~5%

    status VARCHAR(255)                       -- 상품 판매 상태
);


-- 제약 조건
ALTER TABLE products
    ADD CONSTRAINT FK_products_fk_categoryno
        FOREIGN KEY (fk_categoryno) REFERENCES category(categoryno)
            ON DELETE CASCADE;


/* PRODUCTS 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* PRODUCT_IMG 테이블 생성 시작 */

-- 1. 시퀀스 생성 (상품 번호용)
CREATE SEQUENCE prodimg_seq START 1 INCREMENT 1;

-- 2. 테이블 생성
CREATE TABLE product_img (
    product_imgno BIGINT PRIMARY KEY DEFAULT nextval('prodimg_seq'),     -- 상품 이미지 번호 (PK, 시퀀스 기반)
    fk_productno BIGINT NOT NULL,            -- 상품 번호 (FK)
    img varchar(255)                        -- 이미지
);


-- 제약 조건
ALTER TABLE product_img
    ADD CONSTRAINT FK_product_img_fk_productno
        FOREIGN KEY (fk_productno) REFERENCES products(productno)
            ON DELETE CASCADE;


/* PRODUCT_IMG 테이블 생성 끝 */


--------------------------------------------------------------------------------

/* OPTIONS 테이블 생성 시작 */
-- 1. 시퀀스 생성 (상품 번호용)
CREATE SEQUENCE option_seq START 1 INCREMENT 1;

-- 2. 테이블 생성
CREATE TABLE options (
    optionno BIGINT PRIMARY KEY DEFAULT nextval('option_seq'),     -- 상품 번호 (PK, 시퀀스 기반)
    fk_productno BIGINT NOT NULL ,      -- 상품 번호(FK)
    name VARCHAR(255) NOT NULL ,        -- 옵션명
    color VARCHAR(255) NOT NULL ,       -- 색상명
    img VARCHAR(255) NOT NULL           -- 옵션 이미지
);

ALTER TABLE options
    ADD CONSTRAINT FK_options_fk_productno
        FOREIGN KEY (fk_productno) REFERENCES products(productno);

/* OPTIONS 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* DELIVERY 테이블 생성 시작 */

-- 1. 시퀀스 생성
CREATE SEQUENCE delivery_seq START 1 INCREMENT 1;

-- 2. 테이블 생성
CREATE TABLE delivery (
    deliveryno BIGINT PRIMARY KEY DEFAULT nextval('delivery_seq'),  -- 배송지 번호 (PK, 시퀀스 기반)
    fk_user_no BIGINT NOT NULL,                                     -- 외래키: users.user_no
    delivery_name VARCHAR(255) NOT NULL,                            -- 배송지명
    receiver VARCHAR(255) NOT NULL,                                 -- 수령인
    receiver_tel VARCHAR(255) NOT NULL,                             -- 수령인 연락처
    -- [주소] : Address 클래스의 @Embeddable 구성
    zipcode VARCHAR(20),                                            -- 우편번호
    address VARCHAR(255),                                           -- 주소
    detail_address VARCHAR(255),                                    -- 상세주소

    memo VARCHAR(255),                                              -- 배송 요청사항
    is_default VARCHAR(255)                                         -- 기본배송지 여부 (ENUM: DEFAULT, NONE)
);

-- 제약조건
ALTER TABLE delivery
    ADD CONSTRAINT FK_delivery_fk_user_no
        FOREIGN KEY (fk_user_no) REFERENCES users(user_no)
            ON DELETE CASCADE;


/* DELIVERY 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* ORDERS 테이블 생성 시작 */

CREATE SEQUENCE order_seq START 1 INCREMENT 1;

-- 주문 테이블 생성
CREATE TABLE orders (
    orderno BIGINT PRIMARY KEY DEFAULT nextval('order_seq'),  -- 주문 번호 (시퀀스 자동 증가)
    fk_user_no BIGINT NOT NULL,                                -- 회원 번호 (FK)
    fk_deliveryno BIGINT NOT NULL,                             -- 배송지 번호 (FK)
    total_price BIGINT NOT NULL,                               -- 총 주문 금액
    use_point INTEGER NOT NULL DEFAULT 0,                      -- 포인트 사용금액
    reward_point INTEGER NOT NULL,                             -- 포인트 적립금
    delivery_price INTEGER NOT NULL,                           -- 배송비
    total_cnt INTEGER NOT NULL,                                -- 총 수량
    orderday TIMESTAMP DEFAULT CURRENT_TIMESTAMP,              -- 주문 일자
    status VARCHAR(255),                                       -- 주문상태 (ENUM: 결제대기... 환불완료)
    delivery_date TIMESTAMP                                    -- 배송완료일자
);

-- 제약 조건
ALTER TABLE orders
    ADD CONSTRAINT FK_orders_fk_user_no
        FOREIGN KEY (fk_user_no) REFERENCES users(user_no);

ALTER TABLE orders
    ADD CONSTRAINT FK_orders_fk_deliveryno
        FOREIGN KEY (fk_deliveryno) REFERENCES delivery(deliveryno);

/* ORDERS 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* ORDER_DETAIL 테이블 생성 시작 */

CREATE SEQUENCE orderdetail_seq START 1 INCREMENT 1;

-- 주문 상세 테이블 생성
CREATE TABLE order_detail (
    order_detailno BIGINT PRIMARY KEY DEFAULT nextval('orderdetail_seq'), -- 주문 상세 번호 (시퀀스)

    fk_orderno BIGINT NOT NULL,        -- 주문 번호 (FK)
    fk_optionno BIGINT NOT NULL,       -- 옵션 번호 (FK)

    cnt INTEGER NOT NULL,              -- 수량
    price INTEGER NOT NULL            -- 단가
);

ALTER TABLE order_detail
    ADD CONSTRAINT FK_order_detail_fk_orderno
        FOREIGN KEY (fk_orderno) REFERENCES orders(orderno)
            ON DELETE CASCADE;

ALTER TABLE order_detail
    ADD CONSTRAINT FK_order_detail_fk_optionno
        FOREIGN KEY (fk_optionno) REFERENCES options(optionno);

/* ORDER_DETAIL 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* CARTS 테이블 생성 시작 */

CREATE SEQUENCE cart_seq START 1 INCREMENT 1;

CREATE TABLE carts (
    cartno BIGINT PRIMARY KEY DEFAULT nextval('cart_seq'), -- 장바구니 번호 (시퀀스)
    fk_optionno BIGINT NOT NULL ,       -- 옵션 번호 (FK)
    fk_user_no BIGINT NOT NULL ,        -- 회원 번호 (FK)
    cnt INTEGER NOT NULL                -- 수량
);

ALTER TABLE carts
    ADD CONSTRAINT FK_carts_fk_user_no
        FOREIGN KEY (fk_user_no) REFERENCES users(user_no)
            ON DELETE CASCADE;

ALTER TABLE carts
    ADD CONSTRAINT FK_carts_fk_optionno
        FOREIGN KEY (fk_optionno) REFERENCES options(optionno);

/* CARTS 테이블 생성 끝 */

--------------------------------------------------------------------------------

/* REVIEWS 테이블 생성 시작 */

CREATE SEQUENCE review_seq START 1 INCREMENT 1;

CREATE TABLE reviews (
    reviewno BIGINT PRIMARY KEY DEFAULT nextval('review_seq'), -- 리뷰 번호 (시퀀스)
    fk_user_no BIGINT NOT NULL ,        -- 회원 번호 (FK)
    fk_optionno BIGINT NOT NULL ,       -- 옵션 번호 (FK)
    title VARCHAR(255) NOT NULL ,       -- 제목
    content VARCHAR(4000) NOT NULL ,    -- 내용
    score INTEGER NOT NULL ,            -- 별점 1~5
    registerday TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 등록일자
    img VARCHAR(255)                    -- 리뷰 이미지
);

ALTER TABLE reviews
    ADD CONSTRAINT FK_reviews_fk_user_no
        FOREIGN KEY (fk_user_no) REFERENCES users(user_no)
            ON DELETE CASCADE;


ALTER TABLE reviews
    ADD CONSTRAINT FK_reviews_fk_optionno
        FOREIGN KEY (fk_optionno) REFERENCES options(optionno);
/* REVIEWS 테이블 생성 끝 */

--------------------------------------------------------------------------------