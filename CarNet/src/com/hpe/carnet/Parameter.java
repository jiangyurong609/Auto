package com.hpe.carnet;

public class Parameter {
	private String BackgroundFilename;
	private String Filename;
	private String FTPFilename;
	private String BackgroundPORT;
	private String PORT;
	private String WaitTime;
	private String MessageSize;
	private String ServerIP;
	//kernel params
	private String rmem_min;
	private String rmem_default;
	private String rmem_max;
	private String ECR;
	private String Timestamp ;
	private String ECN;
	private String MTU;
	
	public String getBackgroundFilename()
	{
		return BackgroundFilename;
	}
	public String getFilename()
	{
		return Filename;
	}
	public String getBackgroundPORT()
	{
		return BackgroundPORT;
	}
	public String getPORT()
	{
		return PORT;
	}
	public String getWaitTime()
	{
		return WaitTime;
	}
	public String getMessageSize()
	{
		return MessageSize;
	}
	public String getServerIP()
	{
		return ServerIP;
	}
	public String getrmem_min()
	{
		return rmem_min;
	}
	public String getrmem_default()
	{
		return rmem_default;
	}
	public String getrmem_max()
	{
		return rmem_max;
	}
	public String getECR()
	{
		return ECR;
	}
	public String getTimestamp ()
	{
		return Timestamp;
	}
	public String getECN()
	{
		return ECN;
	}
	public String getMTU()
	{
		return MTU;
	}
	public String getFTP()
	{
		return FTPFilename;
	}
	
	public void setBackgroundFilename(String bf)
	{
		BackgroundFilename = bf;
	}
	public void setFilename(String f)
	{
		Filename = f;
	}
	public void setFTPFilename(String f)
	{
		FTPFilename = f;
	}
	public void setBackgroundPORT(String bp)
	{
		BackgroundPORT = bp;
	}
	public void setPORT(String p)
	{
		PORT = p;
	}
	public void setWaitTime(String wt)
	{
		WaitTime = wt;
	}
	public void setMessageSize(String ms)
	{
		MessageSize = ms;
	}
	public void setServerIP(String si)
	{
		ServerIP = si;
	}
	public void setrmem_min(String rm)
	{
		rmem_min = rm;
	}
	public void setrmem_default(String rm)
	{
		rmem_default = rm;
	}
	public void setrmem_max(String rm)
	{
		rmem_max = rm;
	}
	public void setECR(String ecr)
	{
		ECR = ecr;
	}
	public void setTimestamp (String ts)
	{
		Timestamp = ts;
	}
	public void setECN(String ecn)
	{
		ECN = ecn;
	}
	public void setMTU(String mtu)
	{
		MTU = mtu;
	}
	
	
}
