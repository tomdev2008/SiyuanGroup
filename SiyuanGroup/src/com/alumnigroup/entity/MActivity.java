package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 活动实体类
 * 
 * @author Jayin Ton
 * 
 */
public class MActivity implements Serializable {
	public static MActivity create_by_json(String json) {
		MActivity acty = null;
		Gson gson = new Gson();
		try {
			acty = (MActivity) gson.fromJson(json, MActivity.class);
		} catch (Exception e) {
			e.printStackTrace();
			acty = null;
		}
		return acty;
	}

	public static ArrayList<MActivity> create_by_jsonarray(String jsonarray) {
		ArrayList<MActivity> list = new ArrayList<MActivity>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("activities");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public MActivity() {

	}

	/** 活动id **/
	private int id;
	/** 活动发起人id **/
	private int ownerid;
	/** 所属圈子id **/
	private int groupid;
	/** 活动内容 **/
	private String content;
	/** 参与最大人数 **/
	private int maxnum;
	/** 创建活动时间 **/
	private long createtime;
	/** 活动开始时间 **/
	private long starttime;
	/** 活动时长 **/
	private long duration;
	/** 活动状态 */
	private Status status;
	/** 活动状态id 活动状态 0接受报名、1截止报名、2活动结束、3活动取消**/
	private int statusid;
	/** 活动花费 **/
	private String money;
	/** 活动图标 **/
	private String avatar;
	/** 活动名称*/
	private String name;
	/** 活动地点 */
	private String site;
	/** 活动名单 **/
	private List<UserShip> userships;
	/** 圈子拥有者*/
	private User user;
	/** 活动人数*/
	private int numUsership;
	/** 图片数*/
	private int numPictures;
	/** 活动截止申请日期*/
	private long regdeadline;
    /** 图片*/
    private ArrayList<MPicture> pictures;

	public int getNumPictures() {
		return numPictures;
	}

	public void setNumPictures(int numPictures) {
		this.numPictures = numPictures;
	}

	public ArrayList<MPicture> getPictures() {
		return pictures;
	}

	public void setPictures(ArrayList<MPicture> pictures) {
		this.pictures = pictures;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getRegdeadline() {
		return regdeadline;
	}

	public void setRegdeadline(long regdeadline) {
		this.regdeadline = regdeadline;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getNumUsership() {
		return numUsership;
	}

	public void setNumUsership(int numUsership) {
		this.numUsership = numUsership;
	}

	public int getId() {
		return id;
	}

	public int getStatusid() {
		return statusid;
	}

	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<UserShip> getUserships() {
		return userships;
	}

	public void setUserships(List<UserShip> userships) {
		this.userships = userships;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 活动状态码<br>
	 * 活动状态 0接受报名、1截止报名、2活动结束、3活动取消
	 * @author Jayin Ton
	 * 
	 */
	public class Status implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** 活动状态码id **/
		private int id;
		/** 活动状态码描述 **/
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Status [id=" + id + ", name=" + name + "]";
		}
		
	}

	/**
	 * 用户名单?
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public class UserShip implements Serializable {
		/** 参与活动者列表id **/
		private int id;
		/** 用户id **/
		private int userid;
		/** 活动id **/
		private int activityid;
		/** 是否被接受申请参加活动 **/
		private int isaccepted;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUserid() {
			return userid;
		}

		public void setUserid(int userid) {
			this.userid = userid;
		}

		public int getActivityid() {
			return activityid;
		}

		public void setActivityid(int activityid) {
			this.activityid = activityid;
		}

		public int getIsaccepted() {
			return isaccepted;
		}

		public void setIsaccepted(int isaccepted) {
			this.isaccepted = isaccepted;
		}

		@Override
		public String toString() {
			return "UserShip [id=" + id + ", userid=" + userid
					+ ", activityid=" + activityid + ", isaccepted="
					+ isaccepted + "]";
		}

	}

	@Override
	public String toString() {
		return "MActivity [id=" + id + ", ownerid=" + ownerid + ", groupid="
				+ groupid + ", content=" + content + ", maxnum=" + maxnum
				+ ", createtime=" + createtime + ", starttime=" + starttime
				+ ", duration=" + duration + ", status=" + status
				+ ", statusid=" + statusid + ", money=" + money + ", avatar="
				+ avatar + ", name=" + name + ", site=" + site + ", userships="
				+ userships + ", user=" + user + ", numUsership=" + numUsership
				+ ", regdeadline=" + regdeadline + "]";
	}
	
}