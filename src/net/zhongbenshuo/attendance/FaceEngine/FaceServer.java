package net.zhongbenshuo.attendance.FaceEngine;


import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.Rect;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 人脸库操作类，包含注册和搜索
 */

public class FaceServer {
    private static final String TAG = "FaceServer";
    public static final String IMG_SUFFIX = ".jpg";
    private static FaceEngine faceEngine = null;
    private static FaceServer faceServer = null;
    public static String ROOT_PATH = "D:";
    public static final String SAVE_REGISTER_IMG_DIR = File.separator + "registerImg";
    public static final String SAVE_REAL_IMG_DIR = File.separator + "realImg";

    /**
     * 是否正在搜索人脸，保证搜索操作单线程进行
     */
    private boolean isProcessing = false;

    public static FaceServer getInstance() {
        if (faceServer == null) {
            synchronized (FaceServer.class) {
                if (faceServer == null) {
                    faceServer = new FaceServer();
                }
            }
        }
        return faceServer;
    }

    
    /**
     * 用于注册照片人脸
     *
     * @param bgr24   bgr24数据
     * @param width   bgr24宽度
     * @param height  bgr24高度
     * @param face    保存的名字，若为空则使用时间戳
     * @return 是否注册成功
     */
    public static  void  registerBgr24( byte[] bgr24, int width, int height, String user_id,Rect rest,String url) {
//            if (faceEngine == null ||  bgr24 == null || width % 4 != 0 || bgr24.length != width * height * 3) {
//                return ;
//            }

            boolean dirExists = true;
            //图片存储的文件夹
            File imgDir = new File(ROOT_PATH + File.separator + SAVE_REGISTER_IMG_DIR);
            if (!imgDir.exists()) {
                dirExists = imgDir.mkdirs();
            }
            if (!dirExists) {
                return ;
            }
            //人脸检测
           // List<FaceInfo> faceInfoList = new ArrayList<>();
           // int detectCode = faceEngine.detectFaces(bgr24, width, height, ImageFormat.CP_PAF_BGR24, faceInfoList);
                //FaceFeature faceFeature = new FaceFeature();

                //特征提取
                //int extractCode = faceEngine.extractFaceFeature(bgr24, width, height, ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
                try {
                    //保存注册结果（注册图、特征数据）
                        //为了美观，扩大rect截取注册图
                        Rect cropRect = getBestRect(width, height, rest);
                        if (cropRect == null) {
                            return ;
                        }
                        if (((cropRect.right-cropRect.left) & 1) == 1) {
                            cropRect.right--;
                        }
                        if (((cropRect.bottom-cropRect.top) & 1) == 1) {
                            cropRect.bottom--;
                        }
                        File file = new File(imgDir + File.separator + user_id + IMG_SUFFIX);
                       // FileOutputStream fosImage = new FileOutputStream(file);
                        byte[] headBgr24 = ImageUtils.cropBgr24(bgr24, width, height, cropRect);
                      //  System.out.println(Arrays.toString(headBgr24));
                        System.out.println(ROOT_PATH+ File.separator + SAVE_REGISTER_IMG_DIR+"--"+user_id+".jpeg");
                        bgrBytesToJpg(headBgr24,url,0.8f,cropRect);
                        
//                        Bitmap headBmp = ImageUtils.bgrToBitmap(headBgr24, (cropRect.right-cropRect.left), (cropRect.bottom-cropRect.top));
//                        headBmp.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
//                        fosImage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return ;
    }

  

    /**
     * 将图像中需要截取的Rect向外扩张一倍，若扩张一倍会溢出，则扩张到边界，若Rect已溢出，则收缩到边界
     *
     * @param width   图像宽度
     * @param height  图像高度
     * @param srcRect 原Rect
     * @return 调整后的Rect
     */
    private static Rect getBestRect(int width, int height, Rect srcRect) {

        if (srcRect == null) {
            return null;
        }
        Rect rect = new Rect(srcRect);
        //1.原rect边界已溢出宽高的情况
        int maxOverFlow = 0;
        int tempOverFlow = 0;
        if (rect.left < 0) {
            maxOverFlow = -rect.left;
        }
        if (rect.top < 0) {
            tempOverFlow = -rect.top;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.right > width) {
            tempOverFlow = rect.right - width;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.bottom > height) {
            tempOverFlow = rect.bottom - height;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (maxOverFlow != 0) {
            rect.left += maxOverFlow;
            rect.top += maxOverFlow;
            rect.right -= maxOverFlow;
            rect.bottom -= maxOverFlow;
            return rect;
        }
        //2.原rect边界未溢出宽高的情况
        int padding = (rect.bottom-rect.top) / 2;
        //若以此padding扩张rect会溢出，取最大padding为四个边距的最小值
        if (!(rect.left - padding > 0 && rect.right + padding < width && rect.top - padding > 0 && rect.bottom + padding < height)) {
            padding = Math.min(Math.min(Math.min(rect.left, width - rect.right), height - rect.bottom), rect.top);
        }

        rect.left -= padding;
        rect.top -= padding;
        rect.right += padding;
        rect.bottom += padding;
        return rect;
    }


  //将转换后的图片输出到本地
    public static boolean bgrBytesToJpg(byte[] bgr, String afterPath, float jpgQuality,Rect cropRect)  {
        try {
            BufferedImage bufferedImage = new BufferedImage(cropRect.getRight()-cropRect.getLeft(), cropRect.getBottom()-cropRect.getTop(), BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.setRGB(0, 0, cropRect.getRight()-cropRect.getLeft(), cropRect.getBottom()-cropRect.getTop(), convertBgrToColor(bgr), 0, cropRect.getRight()-cropRect.getLeft());
            File file = new File(afterPath);
//            ImageIO.write(bufferedImage, "jpg", file);
 
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
 
            ImageWriter imageWriter = iter.next();
            ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
 
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(jpgQuality);
 
            FileImageOutputStream fileImageOutput = new FileImageOutputStream(file);
            imageWriter.setOutput(fileImageOutput);
            IIOImage iio_image = new IIOImage(bufferedImage, null, null);
            imageWriter.write(null, iio_image, iwp);
            imageWriter.dispose();
 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * bgr转换成int[] rgb
     * @param bgr
     * @return
     */
    private static int[] convertBgrToColor(byte[] bgr) {
        int size = bgr.length;
        if (size == 0) {
            return null;
        } else if (size % 3 != 0) {
            return null;
        } else {
            int[] color = new int[size / 3];
            int index = 0;

            for(int i = 0; i < color.length; ++i) {
                color[i] = (bgr[index + 2] & 255) << 16 | (bgr[index + 1] & 255) << 8 | bgr[index] & 255 | -16777216;
                index += 3;
            }

            return color;
        }
    }

	
   

}
