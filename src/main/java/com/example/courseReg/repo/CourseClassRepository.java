package com.example.courseReg.repo;

import java.util.List;
import java.util.Optional;                    // <-- THÊM DÒNG NÀY

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.courseReg.model.LopHocPhan;

import jakarta.persistence.LockModeType;

public interface CourseClassRepository extends JpaRepository<LopHocPhan, Long> {

  @Query(value = "SELECT lhp.si_so_toi_da FROM lop_hoc_phan lhp WHERE lhp.id = :id", nativeQuery = true)
  Integer findCapacityById(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT l FROM LopHocPhan l WHERE l.id = :id")
  LopHocPhan lockById(@Param("id") Long id);

  // ===== THÊM METHOD NÀY: JPQL fetch-join khungGios =====
  @Query("""
         select distinct l
         from LopHocPhan l
         left join fetch l.khungGios kg
         where l.id = :id
         """)
  Optional<LopHocPhan> findByIdWithKhungGios(@Param("id") Long id);

  // ... các @Query native của bạn giữ nguyên ...
  @Query(value = """
    WITH sched AS (
      SELECT DISTINCT lhp.id AS lop_id,
             CONCAT('Th', kg.thu, ' ',
                    TO_CHAR(kg.gio_bat_dau, 'HH24:MI'), '-', TO_CHAR(kg.gio_ket_thuc, 'HH24:MI')) AS seg,
             kg.thu, kg.gio_bat_dau
      FROM lop_hoc_phan lhp
      JOIN lop_hoc_phan_khung_gio lkg ON lkg.lop_hoc_phan_id = lhp.id
      JOIN khung_gio kg ON kg.id = lkg.khung_gio_id
    ),
    sched_agg AS (
      SELECT lop_id,
             STRING_AGG(seg, ', ' ORDER BY thu, gio_bat_dau) AS schedule
      FROM sched
      GROUP BY lop_id
    ),
    enrolled_agg AS (
      SELECT lhp.id AS lop_id,
             COUNT(DISTINCT CASE WHEN dk.trang_thai = 'da_dang_ky' THEN dk.id END) AS enrolled
      FROM lop_hoc_phan lhp
      LEFT JOIN dang_ky dk ON dk.lop_hoc_phan_id = lhp.id
      GROUP BY lhp.id
    )
    SELECT
      lhp.id                                   AS classId,
      hp.ma_hoc_phan                           AS code,
      hp.ten_hoc_phan                          AS name,
      hp.so_tin_chi                            AS credits,
      lhp.nhom_lop                             AS groupCode,
      COALESCE(gv.ho_ten,'(Đang cập nhật)')    AS teacher,
      COALESCE(sa.schedule,'(Chưa xếp lịch)')  AS schedule,
      lhp.si_so_toi_da                         AS capacity,
      COALESCE(ea.enrolled, 0)                 AS enrolled
    FROM lop_hoc_phan lhp
    JOIN hoc_phan hp           ON hp.id = lhp.hoc_phan_id
    JOIN hoc_ky hk             ON hk.id = lhp.hoc_ky_id
    LEFT JOIN giang_vien gv    ON gv.id = lhp.giang_vien_id
    LEFT JOIN sched_agg sa     ON sa.lop_id = lhp.id
    LEFT JOIN enrolled_agg ea  ON ea.lop_id = lhp.id
    WHERE
      (COALESCE(:q, '') = '' OR
       hp.ma_hoc_phan ILIKE '%'||:q||'%' OR
       hp.ten_hoc_phan ILIKE '%'||:q||'%' OR
       COALESCE(gv.ho_ten,'') ILIKE '%'||:q||'%' OR
       lhp.nhom_lop ILIKE '%'||:q||'%')
      AND (:openOnly = FALSE OR hk.mo_dang_ky = TRUE)
    ORDER BY hp.ma_hoc_phan, lhp.nhom_lop
    """, nativeQuery = true)
  List<CourseClassView> searchByKeyword(@Param("q") String q,
                                        @Param("openOnly") boolean openOnly);

  @Query(value = """
    WITH sched AS (
      SELECT DISTINCT lhp.id AS lop_id,
             CONCAT('Th', kg.thu, ' ',
                    TO_CHAR(kg.gio_bat_dau, 'HH24:MI'), '-', TO_CHAR(kg.gio_ket_thuc, 'HH24:MI')) AS seg,
             kg.thu, kg.gio_bat_dau
      FROM lop_hoc_phan lhp
      JOIN lop_hoc_phan_khung_gio lkg ON lkg.lop_hoc_phan_id = lhp.id
      JOIN khung_gio kg ON kg.id = lkg.khung_gio_id
    ),
    sched_agg AS (
      SELECT lop_id,
             STRING_AGG(seg, ', ' ORDER BY thu, gio_bat_dau) AS schedule
      FROM sched
      GROUP BY lop_id
    ),
    enrolled_agg AS (
      SELECT lhp.id AS lop_id,
             COUNT(DISTINCT CASE WHEN dk.trang_thai = 'da_dang_ky' THEN dk.id END) AS enrolled
      FROM lop_hoc_phan lhp
      LEFT JOIN dang_ky dk ON dk.lop_hoc_phan_id = lhp.id
      GROUP BY lhp.id
    )
    SELECT
      lhp.id                                   AS classId,
      hp.ma_hoc_phan                           AS code,
      hp.ten_hoc_phan                          AS name,
      hp.so_tin_chi                            AS credits,
      lhp.nhom_lop                             AS groupCode,
      COALESCE(gv.ho_ten,'(Đang cập nhật)')    AS teacher,
      COALESCE(sa.schedule,'(Chưa xếp lịch)')  AS schedule,
      lhp.si_so_toi_da                         AS capacity,
      COALESCE(ea.enrolled, 0)                 AS enrolled
    FROM lop_hoc_phan lhp
    JOIN hoc_phan hp           ON hp.id = lhp.hoc_phan_id
    JOIN hoc_ky hk             ON hk.id = lhp.hoc_ky_id
    LEFT JOIN giang_vien gv    ON gv.id = lhp.giang_vien_id
    LEFT JOIN sched_agg sa     ON sa.lop_id = lhp.id
    LEFT JOIN enrolled_agg ea  ON ea.lop_id = lhp.id
    WHERE hk.ma_hoc_ky = :maHocKy
    ORDER BY hp.ma_hoc_phan, lhp.nhom_lop
    """, nativeQuery = true)
  List<CourseClassView> findBySemester(@Param("maHocKy") String maHocKy);
}
