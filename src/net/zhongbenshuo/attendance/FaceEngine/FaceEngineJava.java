package net.zhongbenshuo.attendance.FaceEngine;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;

public class FaceEngineJava {
	public static final String appId = "FxsTMG6wwVwtL4dChvPz11suaVwhJGgYyjdhv43XqKkP";
	public static final String sdkKey = "FRkjDK3oj54tjvEv55Zui91XxJu19xhDm66FPiXoNiVZ";
	public static FaceEngine faceEngine = new FaceEngine("d:\\arcsoft-lib");
	
	/**
	 * 初始化引擎
	 */
	public static void init(){
		
		//激活引擎
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
        }
        
      //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
      	    
      	    
		int initCode = faceEngine.init(engineConfiguration);

        if (initCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }
	}
	/**
	 * 提取特征值
	 * @param imageUrl
	 * @return
	 */
	public static FaceFeature getFaceFeature(String imageUrl){
		//人脸检测
		ImageInfo imageInfo = getRGBData(new File(imageUrl));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        System.out.println(faceInfoList);
        System.out.println("人脸检测码："+detectCode);
        //特征提取
        FaceFeature faceFeature = new FaceFeature();
        int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
        System.out.println("特征提取码："+extractCode);
        return faceFeature;
	}
	/**
	 * 特征比对
	 * @param imageUrl1 图片1
	 * @param imageUrl2 图片2
	 * @return 相似度
	 */
	public static float faceSimilar(String imageUrl1,String imageUrl2){
		FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(getFaceFeature(imageUrl1).getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(getFaceFeature(imageUrl2).getFeatureData());
        FaceSimilar faceSimilar = new FaceSimilar();
        int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        System.out.println("相似度：" + faceSimilar.getScore());
        System.out.println("特征比对码：" + compareCode);
        return faceSimilar.getScore();
	}
	/**
	 * 人脸属性检测
	 * @param imageUrl 图片1
	 */
	public static void configuration (String imageUrl){
		ImageInfo imageInfo = getRGBData(new File(imageUrl));
	    List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
	    int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
		FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        configuration.setSupportFace3dAngle(true);
        configuration.setSupportGender(true);
        configuration.setSupportLiveness(true);
        int processCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, configuration);
	}
	
	
	
}
