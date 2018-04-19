package com.lm.live.base.service.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.lm.live.base.constant.Constants;
import com.lm.live.base.domain.UserAccusationImg;
import com.lm.live.base.service.IUserAccusationImgService;
import com.lm.live.base.service.IPictureService;
import com.lm.live.common.utils.DateUntil;
import com.lm.live.common.utils.FileTypeUtils;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SpringContextListener;

/***
 * 对图片进行操作
 * 
 * @author sx
 * @since 2017/03/22
 * 
 */
@Service("pictureService")
public class PictureServiceImpl implements IPictureService{
	
	@Resource
	private IUserAccusationImgService userAccusationImgService;
	
	/** 动态大图地址 */
	private static final String DYNAMIC_BIG_IMAGE = "bigImage";
	/** 动态小图地址 */
	private static final String DYNAMIC_SMALL_IMAGE = "smallImage";
	
	/** 等比压缩图片比例，0.3 */
	private static final float IMG_RATE = 0.3f; 
	
//	/**
//	 * 图片处理
//	 * @param userId
//	 * @param diaryInfoId 动态id
//	 * @param ratioIndex 图片序号
//	 * @param tempFile 临时文件地址
//	 */
//	public void creatFile(String userId, long diaryInfoId, int ratioIndex, String tempFile) { 
//		LogUtil.log.error("### creatFile-客户端上传图片处理，begin...tempFile=" + tempFile);
//        BufferedOutputStream bos = null;  
//        FileOutputStream fos = null;  
//        try {  
//        	// 文件保存路径
//        	String filepath = SpringContextListener.getContextProValue("cdnUpload", Constants.UPLOAD_FILE_PATH)+Constants.DIARY_IMG_FILE_URI+File.separator;
//	        File files = new File(filepath);
//	        if(!files.exists()){
//	        	files.mkdirs();
//	        }
//        	// 获取文件类型
//        	String type = "png"; // 与客户端约定为png
//            // 生成文件名
//            String groupId = FileTypeUtils.getRandomFileGroupId();
//            String fName = groupId+"."+type; 
//            // 年月文件夹
//            Date nowDate = new Date();
//			String iconDateStr = DateUntil.getFormatDate("yyyyMM", nowDate);
//			
//			// 创建临时存储目录：path + 201703/userId/bigImg(smallImg)
//			String realBigImgPath = iconDateStr + File.separator + userId + File.separator + DYNAMIC_BIG_IMAGE;
//			File dynamicBigDateDir = new File(filepath + realBigImgPath);
//	        if(!dynamicBigDateDir.exists()){
//	        	dynamicBigDateDir.mkdirs();
//	        }
//	        String realSmallImgPath = iconDateStr + File.separator + userId +  File.separator + DYNAMIC_SMALL_IMAGE;
//	        File dynamicSmallDateDir = new File(filepath + realSmallImgPath);
//	        if(!dynamicSmallDateDir.exists()){
//	        	dynamicSmallDateDir.mkdirs();
//	        }
//	        
//	        String bigFName = realBigImgPath + File.separator+fName;
//			String smallFName = realSmallImgPath + File.separator+fName;
//	        
//			String tempImgPath = filepath + tempFile; // 临时文件目录
//			String bigImgPath = filepath + bigFName; // 真实大图目录
//			String smallImgPath = filepath + smallFName; // 真实小图目录
//			// 从temp中写入到真是目录中
//            boolean flag = copyFile(tempImgPath, bigImgPath);
//            LogUtil.log.error("### creatFile-复制图片结果:flag=" + flag + ",临时图片路径：tempImgPath=" + tempImgPath);
//            if(flag) {
//            	File temp = new File(tempImgPath); 
//            	if(temp.exists()) {
//            		LogUtil.log.error("### creatFile-复制图片成功后，删除临时图片，临时图片路径：tempImgPath=" + tempImgPath);
//            		temp.delete();
//            	}
//            }
//			LogUtil.log.error("### creatFile-客户端上传图片处理，写入完毕,bigImgPath="+bigImgPath+",smallImgPath="+smallImgPath);
//			// 压缩图片
//            this.scaleImage(bigImgPath, smallImgPath, 0.3f, "png");
//            // 生成db数据
//            handleDynamicImg(diaryInfoId, ratioIndex, bigFName, smallFName);
//        } catch (Exception e) {  
//            LogUtil.log.error("### creatFile-客户端上传图片处理，写入异常");
//            LogUtil.log.error(e.getMessage(),e);
//        } finally {  
//            if (bos != null) {  
//                try {  
//                    bos.close();  
//                } catch (IOException e1) {  
//                	LogUtil.log.error(e1.getMessage(),e1);  
//                }  
//            }  
//            if (fos != null) {  
//                try {  
//                    fos.close();  
//                } catch (IOException e1) {  
//                	LogUtil.log.error(e1.getMessage(),e1);  
//                }  
//            }  
//        }  
//        LogUtil.log.error("### creatFile-客户端上传图片处理，end!");
//	}
//	
//	
	public void accusationImgDispose(String userId, long aiId, int ratioIndex, String tempImg) {
		BufferedOutputStream bos = null;  
	    FileOutputStream fos = null;  
		try {
			String filepath = SpringContextListener.getContextProValue("cdnUpload", Constants.UPLOAD_FILE_PATH) + Constants.ACCUSATION_IMG_FILE_URL + File.separator;
			//String filepath = "F:\\home\\images\\accusation\\";
			File files = new File(filepath);
			if(!files.exists()){
				files.mkdirs();
			}
			// 获取文件类型
			int imgType = tempImg.lastIndexOf(".");
			String type = tempImg.substring(imgType);
			//String type = "png"; // 与客户端约定为png
			// 生成文件名
			String groupId = FileTypeUtils.getRandomFileGroupId();
			String fName = groupId + type; 
			// 年月文件夹
			Date nowDate = new Date();
			String iconDateStr = DateUntil.getFormatDate("yyyyMM", nowDate);
			
			String realSmallImgPath = iconDateStr + File.separator + userId + File.separator + DYNAMIC_SMALL_IMAGE;
			File SmallDateDir = new File(filepath + realSmallImgPath);
			if (!SmallDateDir.exists()) {
				SmallDateDir.mkdirs();
			}
			String smallFName = realSmallImgPath + File.separator+fName;
			String tempImgPath = filepath + tempImg; // 临时文件目录
			String smallImgPath = filepath + smallFName; // 真实小图目录
			boolean flag = copyFile(tempImgPath, smallImgPath);
			if (flag) {
				File temp = new File(tempImgPath);
				if (temp.exists()) {
					LogUtil.log.error("### accusationImgDispose-复制图片成功后，删除临时图片，临时图片路径：tempImgPath=" + tempImgPath);
					temp.delete();
				}
				// 对举报图片路径入库处理
				handleAccusationImg(aiId, ratioIndex, smallFName);
			}
		} catch (Exception e) {
			LogUtil.log.error("### accusationImgDispose-客户端上传举报图片处理，写入异常");
	        LogUtil.log.error(e.getMessage(),e);
		}finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                	LogUtil.log.error(e1.getMessage(),e1);  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                	LogUtil.log.error(e1.getMessage(),e1);  
                }  
            }  
        }  
	}
	
	/**
	 * @param aiId
	 * @param ratioIndex
	 * @param smallFName
	 */
	private void handleAccusationImg(long aiId, int ratioIndex, String smallFName) {
		UserAccusationImg uai = new UserAccusationImg();
		uai.setAccusationId(aiId);
		uai.setRatioIndex(ratioIndex);
		uai.setUrl(smallFName);
		uai.setUploadTime(new Date());
		this.userAccusationImgService.insert(uai);
	}
	
	/**
	 * 删除重复举报时上传到临时目录的图片
	 * @param tempImg
	 */
	public void deleteAccusationImg(String tempImg) {
		String filepath = SpringContextListener.getContextProValue("cdnUpload", Constants.UPLOAD_FILE_PATH) + Constants.ACCUSATION_IMG_FILE_URL + File.separator;
		File file = new File(filepath + tempImg);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 删除主播秀目录的图片
	 * @param tempImg
	 */
	public void deleteAnchorShowImg(String smallImg,String bigImg) {
		String filepath = SpringContextListener.getContextProValue("cdnUpload", Constants.UPLOAD_FILE_PATH) + Constants.ANCHOR_SHOW_IMG_FILE_URL + File.separator;
		File smallFile = new File(filepath + smallImg);//删除小图
		if(smallFile.exists()){
			smallFile.delete();
		}
		File bigFile = new File(filepath + bigImg);//删除大图
		if(bigFile.exists()){
			bigFile.delete();
		}
		
	}


	/***
	 * 按指定的比例缩放图片
	 * 
	 * @param sourceImagePath
	 *            源地址
	 * @param destinationPath
	 *            改变大小后图片的地址
	 * @param scale
	 *            缩放比例，如1.2
	 * @param format
	 *            图片类型，png
	 */
	private void scaleImage(String sourceImagePath,
			String destinationPath, double scale, String format) {
		File file = new File(sourceImagePath);
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(file);
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			width = parseDoubleToInt(width * scale);
			height = parseDoubleToInt(height * scale);

			Image image = bufferedImage.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage outputImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = outputImage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
			ImageIO.write(outputImage, format, new File(destinationPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/***
	 * 将图片缩放到指定的高度或者宽度
	 * 
	 * @param sourceImagePath
	 *            图片源地址
	 * @param destinationPath
	 *            压缩完图片的地址
	 * @param width
	 *            缩放后的宽度
	 * @param height
	 *            缩放后的高度
	 * @param auto
	 *            是否自动保持图片的原高宽比例
	 * @param format
	 *            图图片格式 例如 jpg
	 */
	private void scaleImageWithParams(String sourceImagePath,
			String destinationPath, int width, int height, boolean auto,
			String format) {
		try {
			File file = new File(sourceImagePath);
			BufferedImage bufferedImage = null;
			bufferedImage = ImageIO.read(file);
			if (auto) {
				ArrayList<Integer> paramsArrayList = getAutoWidthAndHeight(
						bufferedImage, width, height);
				width = paramsArrayList.get(0);
				height = paramsArrayList.get(1);
			}

			Image image = bufferedImage.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage outputImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = outputImage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
			ImageIO.write(outputImage, format, new File(destinationPath));
		} catch (Exception e) {
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

	/***
	 * 
	 * @param bufferedImage
	 *            要缩放的图片对象
	 * @param width_scale
	 *            要缩放到的宽度
	 * @param height_scale
	 *            要缩放到的高度
	 * @return 一个集合，第一个元素为宽度，第二个元素为高度
	 */
	private ArrayList<Integer> getAutoWidthAndHeight(
			BufferedImage bufferedImage, int width_scale, int height_scale) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		double scale_w = getDot2Decimal(width_scale, width);

		System.out.println("getAutoWidthAndHeight width=" + width + "scale_w="
				+ scale_w);
		double scale_h = getDot2Decimal(height_scale, height);
		if (scale_w < scale_h) {
			arrayList.add(parseDoubleToInt(scale_w * width));
			arrayList.add(parseDoubleToInt(scale_w * height));
		} else {
			arrayList.add(parseDoubleToInt(scale_h * width));
			arrayList.add(parseDoubleToInt(scale_h * height));
		}
		return arrayList;

	}

	/***
	 * 返回两个数a/b的小数点后三位的表示
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private double getDot2Decimal(int a, int b) {

		BigDecimal bigDecimal_1 = new BigDecimal(a);
		BigDecimal bigDecimal_2 = new BigDecimal(b);
		BigDecimal bigDecimal_result = bigDecimal_1.divide(bigDecimal_2,
				new MathContext(4));
		Double double1 = new Double(bigDecimal_result.toString());
		System.out.println("相除后的double为：" + double1);
		return double1;
	}
	
	 
	 /**
	  * 复制文件
	  * @param srcFile 源文件地址
	  * @param destFile 目标文件地址
	  * @return
	  */
	 private boolean copyFile(String srcFile, String destFile) {
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            out.flush();
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
	 }


}
