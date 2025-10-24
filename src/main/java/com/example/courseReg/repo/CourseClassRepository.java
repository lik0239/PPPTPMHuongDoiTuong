package com.example.courseReg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.courseReg.model.LopHocPhan;

public interface CourseClassRepository extends JpaRepository<LopHocPhan, Long> {

  @Query(value = """
    SELECT
      lhp.id                                  AS classId,
      hp.ma_hoc_phan                          AS code,
      hp.ten_hoc_phan                         AS name,
      hp.so_tin_chi                           AS credits,
      lhp.nhom_lop                            AS groupCode,
      COALESCE(gv.ho_ten,'(Đang cập nhật)')  AS teacher,
      COALESCE(
        STRING_AGG(
          CONCAT('Th', kg.thu, ' ',
                TO_CHAR(kg.gio_bat_dau, 'HH24:MI'), '-', TO_CHAR(kg.gio_ket_thuc, 'HH24:MI')),
          ', ' ORDER BY kg.thu, kg.gio_bat_dau
        ),
        '(Chưa xếp lịch)'
      )                                       AS schedule,
      lhp.si_so_toi_da                        AS capacity,
      COALESCE(SUM(CASE WHEN dk.trang_thai = 'da_dang_ky' THEN 1 ELSE 0 END), 0) AS enrolled
    FROM lop_hoc_phan lhp
    JOIN hoc_phan hp           ON hp.id = lhp.hoc_phan_id
    JOIN hoc_ky hk             ON hk.id = lhp.hoc_ky_id
    LEFT JOIN giang_vien gv    ON gv.id = lhp.giang_vien_id
    LEFT JOIN lop_hoc_phan_khung_gio lkg ON lkg.lop_hoc_phan_id = lhp.id
    LEFT JOIN khung_gio kg     ON kg.id = lkg.khung_gio_id
    LEFT JOIN dang_ky dk       ON dk.lop_hoc_phan_id = lhp.id
    WHERE
        (:q = '' OR
        hp.ma_hoc_phan ILIKE '%'||:q||'%' OR
        hp.ten_hoc_phan ILIKE '%'||:q||'%' OR
        COALESCE(gv.ho_ten,'') ILIKE '%'||:q||'%' OR
        lhp.nhom_lop ILIKE '%'||:q||'%')
      AND (:openOnly = FALSE OR hk.mo_dang_ky = TRUE)
    GROUP BY lhp.id, hp.ma_hoc_phan, hp.ten_hoc_phan, hp.so_tin_chi, lhp.nhom_lop, gv.ho_ten, lhp.si_so_toi_da
    ORDER BY hp.ma_hoc_phan, lhp.nhom_lop
    """, nativeQuery = true)
  List<CourseClassView> searchByKeyword(@Param("q") String q,
                                        @Param("openOnly") boolean openOnly);

    @Query(value = """
        SELECT
          lhp.id                                  AS classId,
          hp.ma_hoc_phan                          AS code,
          hp.ten_hoc_phan                         AS name,
          hp.so_tin_chi                           AS credits,
          lhp.nhom_lop                            AS groupCode,
          COALESCE(gv.ho_ten,'(Đang cập nhật)')  AS teacher,
          COALESCE(
            STRING_AGG(
              CONCAT('Th', kg.thu, ' ',
                     TO_CHAR(kg.gio_bat_dau, 'HH24:MI'), '-', TO_CHAR(kg.gio_ket_thuc, 'HH24:MI')),
              ', ' ORDER BY kg.thu, kg.gio_bat_dau
            ),
            '(Chưa xếp lịch)'
          )                                        AS schedule,
          lhp.si_so_toi_da                        AS capacity,
          COALESCE(SUM(CASE WHEN dk.trang_thai = 'da_dang_ky' THEN 1 ELSE 0 END), 0) AS enrolled
        FROM lop_hoc_phan lhp
        JOIN hoc_phan hp           ON hp.id = lhp.hoc_phan_id
        JOIN hoc_ky hk             ON hk.id = lhp.hoc_ky_id
        LEFT JOIN giang_vien gv    ON gv.id = lhp.giang_vien_id
        LEFT JOIN lop_hoc_phan_khung_gio lkg ON lkg.lop_hoc_phan_id = lhp.id
        LEFT JOIN khung_gio kg     ON kg.id = lkg.khung_gio_id
        LEFT JOIN dang_ky dk       ON dk.lop_hoc_phan_id = lhp.id
        WHERE hk.ma_hoc_ky = :maHocKy
        GROUP BY lhp.id, hp.ma_hoc_phan, hp.ten_hoc_phan, hp.so_tin_chi, lhp.nhom_lop, gv.ho_ten, lhp.si_so_toi_da
        ORDER BY hp.ma_hoc_phan, lhp.nhom_lop
        """, nativeQuery = true)
    List<CourseClassView> findBySemester(@Param("maHocKy") String maHocKy);
}
