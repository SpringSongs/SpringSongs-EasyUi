package io.github.springsongs.modules.process.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import io.github.springsongs.annotation.Description;
import io.github.springsongs.common.base.SpringBase;

@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "spring_act_vacation", schema = "base_system")
public class SpringActVacation extends SpringBase {
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Description(title = "主键")
	@Column(name = "id")
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Size(max = 45, min = 1)
	@Description(title = "流程实例id")
	@Column(name = "process_instance_id")
	private String processInstanceId;

	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@NotBlank(message="请填写流程Key")
    @Size(max=200, min=1)
    @Description(title ="流程Key")
    @Column(name="proc_def_key")
    private String procDefKey;
    public String getProcDefKey(){
        return  this.procDefKey;
    }
    public void setProcDefKey(String procDefKey){
        this.procDefKey=procDefKey;
    }
    
    
	@Size(max = 36, min = 1)
	@Description(title = "申请人用户id")
	@Column(name = "user_id")
	private String userId;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Size(max = 45, min = 1)
	@Description(title = "真实姓名")
	@Column(name = "true_name")
	private String trueName;

	public String getTrueName() {
		return this.trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	@NotBlank(message = "请填写请假申请原因")
	@Size(max = 45, min = 1)
	@Description(title = "请假申请原因")
	@Column(name = "reason")
	private String reason;

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Description(title = "请假天数")
	@Column(name = "time")
	private int time;

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	@NotBlank(message="请填写开始时间")
    @Size(max=45, min=1)
    @Description(title ="开始时间")
    @Column(name="start_time")
    private String startTime;
    public String getStartTime(){
        return  this.startTime;
    }
    public void setStartTime(String startTime){
        this.startTime=startTime;
    }

    @NotBlank(message="请填写结束时间 ")
    @Size(max=45, min=1)
    @Description(title ="结束时间 ")
    @Column(name="end_time")
    private String endTime;
    public String getEndTime(){
        return  this.endTime;
    }
    public void setEndTime(String endTime){
        this.endTime=endTime;
    }

	@Description(title = "流程状态（0：申请中；1：审批中；2：审批通过；3：审批不通过）")
	@Column(name = "process_status")
	private short processStatus;

	public short getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(short processStatus) {
		this.processStatus = processStatus;
	}

	@Description(title = "submitTime")
	@Column(name = "submitTime")
	private java.util.Date submittime;

	public java.util.Date getSubmittime() {
		return this.submittime;
	}

	public void setSubmittime(java.util.Date submittime) {
		this.submittime = submittime;
	}

}