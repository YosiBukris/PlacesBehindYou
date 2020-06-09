package acs.dal;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import acs.data.ElementEntity;
import acs.data.entityProperties.LocationEntity;

@Repository
public interface ElementDao extends MongoRepository<ElementEntity,String>{

	// select * where name is ?
	public List<ElementEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable); 
	
	public List<ElementEntity> findAllByNameAndActive(
			@Param("name") String name,
			@Param("active") Boolean active,
			Pageable pageable);

	public List<ElementEntity> findAllByType(
			@Param("type") String type,
			Pageable pageable); 

	public List<ElementEntity> findAllByTypeAndActive(
			@Param("type") String type,
			@Param("active") Boolean active,
			Pageable pageable);

	// select * where elementId is ?
	public List<ElementEntity> findAllByElementId(
			@Param("elementId") String elementId,
			Pageable pageable); 
	
	public List<ElementEntity> findOneByElementIdAndActive(
			@Param("elementId") String elementId,
			@Param("active") Boolean active,
			Pageable pageable);


	public List<ElementEntity> findAllByParent_elementId(
			@Param("ParentElementId") String ParentElementId,
			Pageable pageable);
	
	public List<ElementEntity> findAllByParent_elementIdAndActive(
			@Param("ParentElementId") String ParentElementId,
			@Param("active") Boolean active,
			Pageable pageable);
	
	
	public List<ElementEntity> findAllByChildrens_elementId(
			@Param("ChildrensElementId") String ChildrensElementId,
			Pageable pageable);
	
	public List<ElementEntity> findAllByChildrens_elementIdAndActive(
			@Param("ChildrensElementId") String ChildrensElementId,
			@Param("active") Boolean active,
			Pageable pageable);

	public List<ElementEntity> findByLocation(
			@Param("location") LocationEntity location,
			Pageable pageable);

	public List<ElementEntity> findByLocationAndActive(
			@Param("location") LocationEntity location,
			@Param("active") Boolean active,
			Pageable pageable);

	public List<ElementEntity> findAllByActive(
			@Param("active") Boolean active,
			Pageable pageable);


	
//	// select * where name is ? and active is ?
//	public List<ElementEntity> findAllByNameAndActive(
//			@Param("name") String name,
//			@Param("active") boolean active, 
//			Pageable pageable);
//	
//	// select * where type is ?
//	public List<ElementEntity> findAllByType(
//			@Param("type") String type,
//			Pageable pageable); 
//	
//	// select * where type is ? and active is ?
//	public List<ElementEntity> findAllByTypeAndActive(
//			@Param("type") String name,
//			@Param("active") boolean active, 
//			Pageable pageable);
//	
//	// select * where parentId is ?
//	public List<ElementEntity> findAllChildrenByParent_ElementId(
//			@Param("parentId") ElementEntity parentId, 
//			Pageable pageable);
//	
//	// select * where parentId is and active is ?
//		public List<ElementEntity> findAllChildrenByParent_ElementIdAndActive(
//				@Param("parentId") ElementID parentId, 
//				@Param("active") boolean active, 
//				Pageable pageable);
//	
//	// select * where childId is ?
//	public List<ElementEntity> findAllParentsByChildren_ElementId(
//			@Param("parentId") ElementID parentId, 
//			Pageable pageable);
//	
//	// select * where childId is ? and active is true
//	public List<ElementEntity> findAllParentsByChildren_ElementIdAndActive(
//			@Param("parentId") ElementID parentId, 
//			@Param("active") boolean active, 
//			Pageable pageable);
//	
//	// select * where active is ?
//	public List<ElementEntity> findAllByActive(
//			@Param("active") boolean active, 
//			Pageable pageable);
//
//	// select * where lat+
//	public List<ElementEntity> findAllBylocationLatBetweenAndLocationLngBetween(
//			@Param("lat1") float lat1,
//			@Param("lat2") float lat2,
//			@Param("lng1") float lng1,
//			@Param("lng2") float lng2,
//			Pageable pageable);
//	
//	public List<ElementEntity> findAllBylocationLatBetweenAndLocationLngBetweenAndActive(
//			@Param("lat1") float lat1,
//			@Param("lat2") float lat2,
//			@Param("lng1") float lng1,
//			@Param("lng2") float lng2,
//			@Param("active") boolean active, 
//			Pageable pageable);
//	
	
}
