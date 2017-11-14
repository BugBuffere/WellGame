package com.leng.wellgame;

import java.util.ArrayList;
import java.util.List;


/**
 * @author carry
 *
 */
public class WellTree {
	
	private WellTree parent;
	
	private List<WellTree> childs;
	/**
	 * 执行者
	 */
	private int performer;

	private int position = -1;
	
	private int x;
	
	private int y;
	
	private int layer;
	
	/**
	 * 创建树并添加父节点
	 * @param parent
	 */
	public WellTree(WellTree parent) {
		super();
		this.parent = parent;
	}
	
	public WellTree(int x,int y,int performer){
		this(x, y, performer, null);
	}
	
	public WellTree(int x,int y,int performer,WellTree parent){
		if (performer != Config.USER_VALUE && performer != Config.COMPUTER_VALUE) {
			System.out.println(performer);
			throw new RuntimeException("Invalid executor!!!");
		}
		this.x = x;
		this.y = y;
		this.performer = performer;
		this.parent = parent;
	}
	/**
	 * 获取x
	 * @return
	 */
	public int getX(){
		return x;
	}
	/**
	 * 获取y
	 * @return
	 */
	public int getY(){
		return y;
	}
	/**
	 * 设置层数
	 * @param layer
	 */
	public void setLayer(int layer){
		this.layer = layer;
	}
	/**
	 * 层数
	 * @return
	 */
	public int layer(){
		return layer;
	}
	
	public int performer(){
		return performer;
	}
	
	/**
	 * 添加子节点
	 * @param tree
	 */
	public void addChild(WellTree... tree){
		if (tree == null) {
			return;
		}
		if (childs == null) {
			synchronized (WellTree.class) {
				if (childs == null) {
					childs = new ArrayList<WellTree>();
				}
			}
		}
		for (int i = 0; i < tree.length; i++) {
			childs.add(tree[i]);
		}
	}
	/**
	 * 子节点的个数
	 * @return
	 */
	public int childLength(){
		if (childs == null) {
			return 0;
		}
		return childs.size();
	}
	
	/**
	 * 设置父节点
	 * @param parent
	 */
	public void setParent(WellTree parent){
		this.parent = parent;
	}
	/**
	 * 获取父元素
	 * @return
	 */
	public WellTree parent(){
		return parent;
	}
	/**
	 * 获取下一个元素
	 * @return
	 */
	public WellTree next(){
		if (childs == null || childs.isEmpty()) {
			return null;
		}
		return childs.get(position);
	}
	/**
	 * 判断是否还有下一个元素
	 * @return
	 */
	public boolean hashNext(){
		if (childs == null) {
			return false;
		}
		position++;
		return position < childs.size();
	}

	@Override
	public String toString() {
		return "WellTree [parent=" + parent + ", layer=" + layer + ", performer=" + performer + ", position=" + position
				+ ", x=" + x + ", y=" + y + "]";
	}

}
