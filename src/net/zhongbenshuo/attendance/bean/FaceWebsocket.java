package net.zhongbenshuo.attendance.bean;

import java.util.List;

public class FaceWebsocket {
	private String type;//类型 add,delete,update (服务端给客户端的)；ClientResponse（客户端回复给服务端处理结果）
	private Face face;//人脸face的信息
	private int id;//人脸的主键ID
	private List<Integer> updateList;  //人脸的主键IDList
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Face getFace() {
		return face;
	}
	public void setFace(Face face) {
		this.face = face;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Integer> getUpdateList() {
		return updateList;
	}
	public void setUpdateList(List<Integer> updateList) {
		this.updateList = updateList;
	}
	@Override
	public String toString() {
		return "FaceWebsocket [type=" + type + ", face=" + face + ", id=" + id + ", updateList=" + updateList + "]";
	}
	
	
}
