//package com.aptech.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import com.aptech.domain.AppServiceResult;
//import com.aptech.dto.HttpResponse;
//import com.aptech.dto.HttpResponseError;
//import com.aptech.dto.HttpResponseSuccess;
//import com.aptech.dto.singer.SingerDto;
//import com.aptech.service.ICommonService;
//
//@RestController
//@RequestMapping("/common")
//public class CommonController {
//	
//	private ICommonService commonService;
//	
//	@Autowired
//	public CommonController(ICommonService commonService) {
//		this.commonService = commonService;
//	}
//	
//	@GetMapping(path = "/search-singer")
//	public ResponseEntity<HttpResponse> searchSinger(@RequestParam String searchString) {
//
//		AppServiceResult<List<SingerDto>> result = commonService.SearchSinger(searchString);
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<SingerDto>>(result.getData()))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}
//
//}
