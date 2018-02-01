package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 * @author xinsuila8
 *
 */
public class PageResult implements Serializable {
	
	private long total; //总记录数
	private List rows;	//当前页结果  由于这个地方得实体类是公用的,所以泛型一般不加
	
	
	//为了以后创建对象方便,我们加一个有参构造
	public PageResult(long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	
	
	
	

}
