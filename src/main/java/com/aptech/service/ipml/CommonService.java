//package com.aptech.service.ipml;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.aptech.constant.AppError;
//import com.aptech.domain.AppServiceResult;
//import com.aptech.dto.singer.SingerDto;
//import com.aptech.entity.Singer;
//import com.aptech.repository.ArtistRepository;
//import com.aptech.service.ICommonService;
//
//@Service
//public class CommonService implements ICommonService {
//
//	private ArtistRepository singerRepository;
//	
//	@Autowired
//	public CommonService(ArtistRepository singerRepository) {
//		this.singerRepository = singerRepository;
//	}
//
//	@Override
//	public AppServiceResult<List<SingerDto>> SearchSinger(String searchString) {
//		try {
//			List<Singer> singers = singerRepository.findByStageNameContaining(searchString);
//			
//			List<SingerDto> result = new ArrayList<SingerDto>();
//			
//			singers.forEach(item -> {
//				result.add(new SingerDto(item.getId(), item.getStageName(), item.getAvatarUrl()));
//			});
//			
//			return new AppServiceResult<List<SingerDto>>(true, 0, "Success", result);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new AppServiceResult<List<SingerDto>>(false, AppError.Unknown.errorCode(),
//					AppError.Unknown.errorMessage(), null);
//		}
//		
//	}
//
//}
