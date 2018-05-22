package com.lm.live.base.service.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.enums.ErrorCode;
import com.lm.live.base.exception.BaseBizException;
import com.lm.live.base.service.IFileUploadService;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.FileTypeUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;
import com.lm.live.userbase.domain.UserInfoDo;
import com.lm.live.userbase.service.IUserBaseService;

/**
 * 图片上传服务
 * @author shao.xiang
 * @data 2018年4月15日
 */
@Service("fileUploadService")
public class FileUploadServiceImpl implements IFileUploadService {
	
	@Resource
	private IUserBaseService userBaseService;

	@Override
	public void uploadFile(HttpServletRequest request, String userId) throws Exception{
		LogUtil.log.info("### F1-userId :" + userId);
		if(!StringUtils.isEmpty(userId)) {
			// my-todo 这里的路径要修改：应该上传至cdn的路径，调试时使用本地路径
//			String filepath = Constants.cdnPath + Constants.ICON_IMG_FILE_URI+File.separator;
			String filepath = "http://192.168.1.70:8616/home/lm/data/uploadfiles/" + Constants.ICON_IMG_FILE_URI+File.separator;
	        File files = new File(filepath);
	        if(!files.exists()){
	        	files.mkdirs();
	        }
	        LogUtil.log.info("### F1-mkdirs :" + filepath);
	        //创建一个通用的多部分解析器  
			CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());
			if(cmr.isMultipart(request)){
				 //转换成多部分request    
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
	            //获取request中的所有文件名  
	            Iterator<String> it = multiRequest.getFileNames();
	            while(it.hasNext()){
	            	//取得上传文件  
	                MultipartFile file = multiRequest.getFile(it.next());
	                //获取文件类型
	            	String type = FileTypeUtils.getFileByFile(file);
	                //生成文件名
	                String groupId = FileTypeUtils.getRandomFileGroupId();
	                String fName = groupId+"."+type;
	                
	                Date nowDate = new Date();
					String iconDateStr = DateUntil.getFormatDate("yyyyMMdd", nowDate);
					
					// 按天创建目录
					File iconDateDir = new File(filepath+iconDateStr);
			        if(!iconDateDir.exists()){
			        	iconDateDir.mkdirs();
			        }
					// 头像地址按目录分类
					fName = iconDateStr+File.separator+fName;
					LogUtil.log.info("### F1-file :" + fName + ",file= " + file.getName() +
							"," + file.getOriginalFilename() + "," + file.getSize());
	                if(type != null && (type.equals("jpg") || type.equals("png") || type.equals("gif") || type.equals("JPEG"))){
	                	File fileP = new File(filepath+fName);
						file.transferTo(fileP);
						UserInfoDo dbUserInfo = userBaseService.getUserByUserId(userId);
						int isModifyInfo = dbUserInfo.getIsModifyInfo();
						if (isModifyInfo == 0) { // 检测是否可以修改头像
							throw new BaseBizException(ErrorCode.ERROR_10003);
						} 
						LogUtil.log.info("### F1-updata..."+ fName);
						userBaseService.updateIcon(userId, fName);
	                } else {
	                	throw new BaseBizException(ErrorCode.ERROR_10002);
	                }
	            }
			}
			LogUtil.log.info("### F1-updata...cmr is error");
		}
	}

	@Override
	public JSONObject uploadImgs(HttpServletRequest request, String userId) throws Exception {
		JSONObject json = new JSONObject();
		if(!StringUtils.isEmpty(userId)){
			String filepath = Constants.cdnPath + Constants.ACCUSATION_IMG_FILE_URL + File.separator;
			File files = new File(filepath);
			if(!files.exists()){
				files.mkdirs();
			}
			 //创建一个通用的多部分解析器  
			 CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getSession().getServletContext());
			 if(cmr.isMultipart(request)){
				//返回客户端的临时图片路径
				JSONArray imgPath = new JSONArray();
				//转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				//获取request中的所有文件名  
	            Iterator<String> it = multiRequest.getFileNames();
	            while(it.hasNext()){
	            	JSONObject imgs = new JSONObject();
	            	MultipartFile file = multiRequest.getFile(it.next());
	            	//String fileName = file.getName();
	            	//获取文件类型
	            	String type = FileTypeUtils.getFileByFile(file);
	            	//生成文件名
	                String groupId = FileTypeUtils.getRandomFileGroupId();
	                String fName = groupId+"."+type; 
	                Date nowDate = new Date();
					String iconDateStr = DateUntil.getFormatDate("yyyyMM", nowDate);
					String realTempImgPath = iconDateStr + File.separator + userId + File.separator + Constants.TEMP_IMAGE;
					File tempSmallDateDir = new File(filepath + realTempImgPath);
			        if(!tempSmallDateDir.exists()){
			        	tempSmallDateDir.mkdirs();
			        }
			        String realBigImgPath = iconDateStr + File.separator + userId + File.separator + Constants.BIG_IMAGE;
					File bigImgDateDir = new File(filepath + realBigImgPath);
			        if(!bigImgDateDir.exists()){
			        	bigImgDateDir.mkdirs();
			        }
			        String tempFName = realTempImgPath + File.separator + fName;
			        String bigFName = realBigImgPath + File.separator + fName;
			        imgs.put("img", tempFName);
			        imgPath.add(imgs);
			        json.put("imgs", imgPath.toString()); // 返回客户端数据
			        boolean fileType = (type != null && (type.equals("jpg") || type.equals("png") || type.equals("gif") || type.equals("JPEG")));
			        File fileTemp = new File(filepath+tempFName);
					if(file != null && file.getSize() > Constants.MAX_SIZE){
						if(fileType){
							File fileBig = new File(filepath+bigFName);
							file.transferTo(fileBig);
							// 压缩图片
						    this.scaleImage(fileBig, fileTemp, Constants.IMG_RATE, type);
						    if(fileBig.exists()){
						    	LogUtil.log.error("### uploadImgs-压缩图片成功后，删除大图，大图路径：bigImgPath=" + filepath + bigFName);
						    	fileBig.delete();
						    }
						}else{
							throw new BaseBizException(ErrorCode.ERROR_10004);
						}
					}else{
						if(fileType){
							file.transferTo(fileTemp);
						}
					}
	            }
			 }
		}
		LogUtil.log.error("####F3 uploadImg-end:json=" + json.toString());
		return json;
	}
	/**
	 * @param sourceFile
	 * @param desFile
	 * @param scale
	 * @param format
	 */
	private void scaleImage(File sourceFile, File desFile, double scale, String format) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(sourceFile);
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			width = parseDoubleToInt(width * scale);
			height = parseDoubleToInt(height * scale);

			Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = outputImage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
			ImageIO.write(outputImage, format, desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 将double类型的数据转换为int，四舍五入原则
	 * 
	 * @param sourceDouble
	 * @return
	 */
	private int parseDoubleToInt(double sourceDouble) {
		int result = 0;
		result = (int) sourceDouble;
		return result;
	}
}
