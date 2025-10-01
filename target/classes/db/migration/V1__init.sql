-- Flyway V1 – Tao bang cho he thong dang ky hoc phan (10 bang)

create table if not exists khoa (
  id bigserial primary key,
  ma_khoa varchar(20) unique not null,
  ten_khoa text not null
);

create table if not exists sinh_vien (
  id bigserial primary key,
  ma_so_sinh_vien varchar(20) unique not null,
  ho_ten text not null,
  khoa_id bigint references khoa(id),
  khoa_nhap_hoc int,
  gioi_han_tin_chi int default 20
);

create table if not exists giang_vien (
  id bigserial primary key,
  ma_so_giang_vien varchar(20) unique not null,
  ho_ten text not null,
  khoa_id bigint references khoa(id)
);

create table if not exists hoc_phan (
  id bigserial primary key,
  ma_hoc_phan varchar(20) unique not null,
  ten_hoc_phan text not null,
  so_tin_chi int not null check (so_tin_chi > 0),
  khoa_id bigint references khoa(id)
);

create table if not exists hoc_phan_tien_quyet (
  hoc_phan_id bigint references hoc_phan(id) on delete cascade,
  hoc_phan_tien_quyet_id bigint references hoc_phan(id) on delete cascade,
  primary key (hoc_phan_id, hoc_phan_tien_quyet_id)
);

create table if not exists hoc_ky (
  id bigserial primary key,
  ma_hoc_ky varchar(20) unique not null,
  ngay_bat_dau date not null,
  ngay_ket_thuc date not null,
  mo_dang_ky boolean default false
);

create table if not exists khung_gio (
  id bigserial primary key,
  thu int not null check (thu between 1 and 7),
  gio_bat_dau time not null,
  gio_ket_thuc time not null,
  check (gio_bat_dau < gio_ket_thuc)
);

create table if not exists lop_hoc_phan (
  id bigserial primary key,
  hoc_phan_id bigint not null references hoc_phan(id),
  hoc_ky_id bigint not null references hoc_ky(id),
  nhom_lop varchar(10) not null,
  giang_vien_id bigint references giang_vien(id),
  si_so_toi_da int not null check (si_so_toi_da > 0),
  ghi_chu text,
  unique (hoc_phan_id, hoc_ky_id, nhom_lop)
);

create table if not exists lop_hoc_phan_khung_gio (
  lop_hoc_phan_id bigint references lop_hoc_phan(id) on delete cascade,
  khung_gio_id bigint references khung_gio(id) on delete cascade,
  primary key (lop_hoc_phan_id, khung_gio_id)
);

create table if not exists dang_ky (
  id bigserial primary key,
  sinh_vien_id bigint not null references sinh_vien(id),
  lop_hoc_phan_id bigint not null references lop_hoc_phan(id),
  trang_thai varchar(12) not null check (trang_thai in ('da_dang_ky','da_huy','cho')),
  thoi_diem_dang_ky timestamptz default now(),
  unique (sinh_vien_id, lop_hoc_phan_id)
);

create index if not exists ix_dang_ky_sinh_vien on dang_ky(sinh_vien_id);
create index if not exists ix_dang_ky_lop_hoc_phan on dang_ky(lop_hoc_phan_id);
create index if not exists ix_lop_hoc_phan_hoc_ky on lop_hoc_phan(hoc_ky_id);
create index if not exists ix_lop_hoc_phan_hoc_phan on lop_hoc_phan(hoc_phan_id);
create index if not exists ix_khung_gio_thu_gio on khung_gio(thu, gio_bat_dau, gio_ket_thuc);
