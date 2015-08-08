package example.com.opengltest.object.bone;

import example.com.opengltest.object.ObjModel;
import example.com.opengltest.util.MatrixState;

public class Robot {
    //机器人的各个部件
    BodyPart bRoot, bBody, bHead, bLeftTop, bLeftBottom, bRightTop, bRightBottom,
            bRightLegTop, bRightLegBottom, bLeftLegTop, bLeftLegBottom, bLeftFoot, bRightFoot;
    BodyPart[] bpArray;
    //用于绘制的最终矩阵数组
    float[][] fianlMatrixForDrawArray;
    //用于绘制的临时矩阵数组
    float[][] fianlMatrixForDrawArrayTemp;
    Object lock = new Object();//绘制数据锁

    public Robot(ObjModel[] objectArray) {
        bRoot = new BodyPart(0, 0, 0, null, 0, this);//
        bBody = new BodyPart(0.0f, 0.938f, 0.0f, objectArray[0], 1, this);//不动点坐标（x,z,y）
        bHead = new BodyPart(0.0f, 1.00f, 0.0f, objectArray[1], 2, this);
        bLeftTop = new BodyPart(0.107f, 0.938f, 0.0f, objectArray[2], 3, this);
        bLeftBottom = new BodyPart(0.105f, 0.707f, -0.033f, objectArray[3], 4, this);
        bRightTop = new BodyPart(-0.107f, 0.938f, 0.0f, objectArray[4], 5, this);
        bRightBottom = new BodyPart(-0.105f, 0.707f, -0.033f, objectArray[5], 6, this);
        bRightLegTop = new BodyPart(-0.068f, 0.6f, 0.02f, objectArray[6], 7, this);
        bRightLegBottom = new BodyPart(-0.056f, 0.312f, 0f, objectArray[7], 8, this);
        bLeftLegTop = new BodyPart(0.068f, 0.6f, 0.02f, objectArray[8], 9, this);
        bLeftLegBottom = new BodyPart(0.056f, 0.312f, 0f, objectArray[9], 10, this);
        bLeftFoot = new BodyPart(0.068f, 0.038f, 0.033f, objectArray[10], 11, this);
        bRightFoot = new BodyPart(-0.068f, 0.038f, 0.033f, objectArray[11], 12, this);
        bpArray = new BodyPart[]{//所有的骨骼列表
                bRoot, bBody, bHead, bLeftTop, bLeftBottom, bRightTop, bRightBottom,
                bRightLegTop, bRightLegBottom, bLeftLegTop, bLeftLegBottom, bLeftFoot, bRightFoot
        };
        //每个骨骼一个矩阵
        fianlMatrixForDrawArray = new float[bpArray.length][16];
        fianlMatrixForDrawArrayTemp = new float[bpArray.length][16];
        bRoot.addChild(bBody);
        bBody.addChild(bHead);
        bBody.addChild(bLeftTop);
        bBody.addChild(bRightTop);
        bLeftTop.addChild(bLeftBottom);
        bRightTop.addChild(bRightBottom);
        bBody.addChild(bRightLegTop);
        bRightLegTop.addChild(bRightLegBottom);
        bBody.addChild(bLeftLegTop);
        bLeftLegTop.addChild(bLeftLegBottom);
        bLeftLegBottom.addChild(bLeftFoot);
        bRightLegBottom.addChild(bRightFoot);
        //级联计算每个子骨骼在父骨骼坐标系中的原始坐标，并且将平移信息记录进矩阵
        bRoot.initFatherMatrix();
        //层次级联更新骨骼矩阵的方法真实的平移信息，相对于世界坐标系
        bRoot.updateBone();
        //层次级联计算子骨骼初始情况下在世界坐标系中的变换矩阵的逆矩阵
        bRoot.calMWorldInitInver();
    }

    public void updateState() {//在线程中调用此方法
        bRoot.updateBone();
    }

    public void backToInit() {    //在线程中调用此方法
        bRoot.backToIInit();
    }

    public void flushDrawData() {//在线程中调用此方法
        synchronized (lock) {//加锁将主数据拷贝进绘制数据
            for (BodyPart bp : bpArray) {
                bp.copyMatrixForDraw();
            }
        }
    }

    public void drawSelf() {
        synchronized (lock) {//绘制前将绘制数据拷贝进临时数据
            fianlMatrixForDrawArrayTemp = fianlMatrixForDrawArray.clone();
        }
        MatrixState.pushMatrix();
        //从根部件开始绘制
        bRoot.drawSelf(fianlMatrixForDrawArrayTemp);
        MatrixState.popMatrix();
    }
}
