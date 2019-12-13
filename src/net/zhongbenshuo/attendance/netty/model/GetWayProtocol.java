package net.zhongbenshuo.attendance.netty.model;

import java.util.Arrays;



public class GetWayProtocol {
	private byte commd;
	private byte[] len;
	private String getWay;
	private byte[] getWayASCII;
	private String datas;
	private byte[] data;
	private int type;
	private String sevenBdata;
	

	public String getSevenBdata() {
		return sevenBdata;
	}

	public void setSevenBdata(String sevenBdata) {
		this.sevenBdata = sevenBdata;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public byte getCommd() {
		return commd;
	}

	public void setCommd(byte commd) {
		this.commd = commd;
	}

	public byte[] getLen() {
		return len;
	}

	public void setLen(byte[] len) {
		this.len = len;
	}

	public String getGetWay() {
		return getWay;
	}

	public void setGetWay(String getWay) {
		this.getWay = getWay;

		if ((getWay != null) & (getWay.length() == 11)) {
			String threadimei_ASCII = "3" + getWay.substring(0, 1) + "3"
					+ getWay.substring(1, 2) + "3" + getWay.substring(2, 3)
					+ "3" + getWay.substring(3, 4) + "3"
					+ getWay.substring(4, 5) + "3" + getWay.substring(5, 6)
					+ "3" + getWay.substring(6, 7) + "3"
					+ getWay.substring(7, 8) + "3" + getWay.substring(8, 9)
					+ "3" + getWay.substring(9, 10) + "3"
					+ getWay.substring(10, 11);
			byte[] arri = new byte[threadimei_ASCII.length() / 2];
			for (int i = 0; i < (threadimei_ASCII.length() / 2); i++) {
				arri[i] = (byte) Frequent.HexS2ToInt(threadimei_ASCII
						.substring(2 * i, 2 * i + 2));
			}
			this.getWayASCII = arri;
		}
	}

	public byte[] getGetWayASCII() {
		return getWayASCII;
	}

	public void setGetWayASCII(byte[] getWayASCII) {
		this.getWayASCII = getWayASCII;
		if (getWayASCII != null)
			this.getWay = Frequent.BytesToAsciis(getWayASCII);
	}

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
		if (datas != null) {
			byte[] arri = new byte[datas.length() / 2];
			for (int i = 0; i < (datas.length() / 2); i++) {
				arri[i] = (byte) Frequent.HexS2ToInt(datas.substring(2 * i,
						2 * i + 2));
			}
			this.data = arri;
		}
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
		if (data != null)
			this.datas = Frequent.BytesToHexs(data);
	}

	@Override
	public String toString() {
		return "GetWayProtocol [commd=" + commd + ", len="
				+ Arrays.toString(len) + ", getWay=" + getWay
				+ ", getWayASCII=" + Arrays.toString(getWayASCII) + ", datas="
				+ datas + ", data=" + Arrays.toString(data) + ", type=" + type
				+ ", sevenBdata=" + sevenBdata + "]";
	}

	
}
