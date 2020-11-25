package io.github.springsongs.modules.process.repo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.springsongs.modules.process.domain.SpringActVacation;

@Repository
public interface SpringActVacationRepo extends JpaRepository<SpringActVacation, String> {
	/**
	 * 分页查询
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<SpringActVacation> findAll(Specification<SpringActVacation> spec, Pageable pageable);
	
	/**
	 *
	 * IN查询
	 * 
	 * @param ids
	 * @return List<BaseSpringActVacationEntity>
	 * @see [相关类/方法]（可选）
	 * @since [产品/模块版本] （可选）
	 */
	@Query(value = "from SpringActVacation where id in (:ids)")
	public List<SpringActVacation> findInIds(@Param(value = "ids") List<String> ids);
	
}
