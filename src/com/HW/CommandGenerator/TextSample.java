package com.HW.CommandGenerator;

import java.awt.Color;
import java.awt.image.VolatileImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AllPermission;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class TextSample {
   static int length = 0;
   static int max = 1000;
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("命令生成");
		shell.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox (shell, style);
				messageBox.setText ("Information");
				messageBox.setMessage ("Close the shell?");
				event.doit = messageBox.open () == SWT.YES;
			}
		});
		
		
		final Text contentResult =  new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		contentResult.setBounds(700, 35, 325, 400);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 5, 300, 30);
		label.setText("源命令");

		final Text content = new Text(shell, SWT.WRAP );
		content.setBounds(5, 35, 325, 200);
		
		Label label2 = new Label(shell, SWT.NONE);
		label2.setBounds(350, 5, 300, 30);
		label2.setText("想要替換部分的匹配字段");
		

		final Text content2 = new Text(shell, SWT.WRAP );
		content2.setBounds(350, 35, 325, 150);
		
		
		Label label3 = new Label(shell, SWT.NONE);
		label3.setBounds(360, 190, 300, 30);
		label3.setText("参数变化范围");
		
	
		
		final Spinner spinnerStart = new Spinner (shell, SWT.BORDER);
		spinnerStart.setMinimum(0);
		spinnerStart.setMaximum(max);
		spinnerStart.setSelection(0);
		spinnerStart.setIncrement(1);
		spinnerStart.setPageIncrement(100);
		spinnerStart.setLocation(365,220);
		spinnerStart.pack();
		
		final Spinner spinnerEnd = new Spinner (shell, SWT.BORDER);
		spinnerEnd.setMinimum(0);
		spinnerEnd.setMaximum(max);
		spinnerEnd.setSelection(max);
		spinnerEnd.setIncrement(1);
		spinnerEnd.setPageIncrement(100);
		spinnerEnd.setLocation(500,220);
		spinnerEnd.pack();
		  
		
		
		
		final Button selectAll = new Button(shell, SWT.NONE);
		selectAll.setText("ACTION");
		selectAll.setBounds(300, 250, 75, 25);
		selectAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				String src = content.getText().trim();
				String[] s = src.split(";");
   
				String ss = content2.getText().trim();
				String[] target = ss.split("\r\n");
				String str = target[0];
				int len = str.length();
				int index = len - 1;
				while(str.charAt(index)=='*')
				{
					index--;
				}
				length = len -1 -index;
				int start = Integer.parseInt(spinnerStart.getText());
				int end =  Integer.parseInt(spinnerEnd.getText());
				StringBuilder sb = new StringBuilder(length);
				for(int i = 0;i<length;i++)
					sb.append("9");
				max = Integer.parseInt(sb.toString());
				
				if(start<0 |start >max | end<0 |end >max|start > end)
				{
					int style = SWT.ERROR;
					MessageBox messageBox = new MessageBox (shell, style);
					messageBox.setText ("Error");
					messageBox.setMessage ("参数范围错误,请重新填入!");
					se.doit = messageBox.open () == SWT.YES;
				}
				else
				{
					int style = SWT.OK;
					MessageBox messageBox = new MessageBox (shell, style);
					messageBox.setText ("OK");
					messageBox.setMessage ("准备就绪,正在执行!");
					se.doit = messageBox.open () == SWT.YES;
				
				File out = new File("out.txt");
				FileWriter fos = null;
				try {
					fos = new FileWriter(out);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				BufferedWriter bos = new BufferedWriter(fos);
				StringBuilder sbb = new StringBuilder();
		         sb = new StringBuilder();
		        for(int i = start;i<=end;i++)
		        {
					for(int j=0;j<target.length;j++)
					{
						String temp = target[j].substring(0, target[j].length()-length);
						if(src.contains(temp))
						{
							String[] t = src.split(temp);
							String f = "%0" + length + "d";
							String string = String.format(f,i);
							for(int k = 0;k<t.length;k++)
							{
								if(k==0)
								{
									sbb.append(t[k]);
									sbb.append(temp);
									continue;
								}
								if(k==t.length-1)
								{
									sbb.append(string).append(t[k].substring(length, t[k].length()));
								}
								else
								{
									sbb.append(string).append(t[k].substring(length, t[k].length()));
									sbb.append(temp);
									
								}
							}
							src = sbb.toString();
							sbb=sbb.delete(0, src.length());
						}
					}
					sb = sb.append(src+"\r\n");
					try {
						contentResult.setText(sb.toString());
						sb.delete(0, sb.length());
						bos.write(src+"\r\n");
						bos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		    
		        int style2 = SWT.OK;
				MessageBox messageBox2 = new MessageBox (shell, style2);
				messageBox2.setText ("OK");
				messageBox2.setMessage ("结果保存在: "+out.getAbsolutePath());
				se.doit = messageBox2.open () == SWT.YES;
			}
			}
		});
		
//		Button select = new Button(shell2, SWT.NONE);
//		select.setText("全选");
//		select.setBounds(90, 225, 75, 25);
//		select.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				
//				contentResult.selectAll();
//			}
//
//		});
//
//
//		Button cancel = new Button(shell2, SWT.NONE);
//		cancel.setText("取消全选");
//		cancel.setBounds(90, 225, 75, 25);
//		cancel.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				// 如果有选中的文本
//				if (contentResult.getSelectionCount() > 0)
//					contentResult.clearSelection();// 清除选择
//			}
//
//		});

//		Button copy = new Button(shell2, SWT.NONE);
//		copy.setText("复制");
//		copy.setBounds(175, 225, 75, 25);
//		copy.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				// 复制到剪切板
//				contentResult.copy();
//			}
//
//		});

	
		shell.layout();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
