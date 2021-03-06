package com.hy.java.utility;

import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.hy.java.utility.common.SystemTime;
import com.hy.java.utility.gui.CardFrame;
import com.hy.java.utility.gui.GridBagPanel;
import com.hy.java.utility.math.probabilityandstatistics.NormalDistribution;

public class IntegrationTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IntegrationTest integration_test = new IntegrationTest();
		integration_test.draw_nd(-10.0, 5555.0, 5000000);
		integration_test.world_frame();
	}

	/**
	 * 多线程编程
	 * 
	 * 放到developing分支上。多次commit
	 */
	private void world_frame() {
		CardFrame card_frame = new CardFrame("World", 800, 600);
		/*
		 * panel
		 */
		GridBagPanel panel = new GridBagPanel("main_panel");
		card_frame.addGridBagPanel(panel);
		/*
		 * panel内容
		 */
		TextArea main_text_area = new TextArea();
		main_text_area.setName("main_text_area");
		panel.addComponent(main_text_area, main_text_area.getName(), 1, 1, 1, 1, 0, 0, true);
		// 创建一个线程池
		long start_time = SystemTime.currentTimeMillis();
		main_text_area.append("世界于" + SystemTime.formatTime(start_time) + "开始运行\n");
		int task_size = 5;
		ExecutorService pool = Executors.newFixedThreadPool(task_size);
		// ScheduledExecutorService pool1 = Executors.newScheduledThreadPool(taskSize);
		// 创建多个有返回值的任务
		List<FutureTask<String>> task_list = new ArrayList<>();
		for (int i = 0; i < task_size; i++) {
			Callable<String> c = new SampleUnit(i + " ", panel, main_text_area.getName());
			FutureTask<String> future_task = new FutureTask<>(c);
			task_list.add(future_task);
			pool.submit(future_task);
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// pool1.scheduleAtFixedRate(futureTask, 0, 1000, TimeUnit.SECONDS);
		}
		// 关闭线程池
		pool.shutdown();
		// 获取所有并发任务的运行结果
		for (FutureTask<String> f : task_list) {
			// 从Future对象上获取任务的返回值，并输出到控制台
			try {
				String result = f.get();
				main_text_area.append(result + "\n");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end_time = SystemTime.currentTimeMillis();
		main_text_area.append("世界于" + SystemTime.formatTime(end_time) + "结束运行，运行时间" + (end_time - start_time) + "毫秒\n");
		/*
		 * help
		 */
		JFrame help_frame = new JFrame();
		help_frame.setSize(300, 200);
		card_frame.setHelpContents(help_frame);
		/*
		 * about
		 */
		Dialog about_dialog = new Dialog(card_frame);
		about_dialog.setSize(300, 200);
		card_frame.setAbout(about_dialog);
	}

	private void draw_nd(final double mean, final double variance, final int size) {
		final CardFrame card_frame = new CardFrame("Test", 800, 600);
		// panel
		GridBagPanel panel = new GridBagPanel("panel") {
			/**
			 * serialVersionUID
			 */
			private static final long serialVersionUID = 884955750558840773L;

			@Override
			public void paint(Graphics graphics) {
				// 必须先调用父类的paint方法
				super.paint(graphics);
				// 用画笔Graphics，在panel上画图
				// X轴
				graphics.drawLine(0, card_frame.getHeight() / 2, card_frame.getWidth(), card_frame.getHeight() / 2);
				// Y轴
				graphics.drawLine(card_frame.getWidth() / 2, 0, card_frame.getWidth() / 2, card_frame.getHeight());
				// 生成正态分布随机数
				double[] nums = IntegrationTest.this.randomND(mean, variance, size);
				// 找出样本最小值、最大值
				double min = 0;
				double max = 0;
				for (int i = 0; i < nums.length; i++) {
					if (nums[i] < min) {
						min = nums[i];
					}
					if (nums[i] > max) {
						max = nums[i];
					}
				}
				// 根据最小值、最大值，将随机数所在区间对称化，并记录区间宽度
				double width = 1.00;
				if (mean - min > max - mean) {
					width = 2 * (mean - min);
				} else {
					width = 2 * (max - mean);
					min = max - width;
				}
				// 记录每个点的频率
				int[] fs = new int[card_frame.getWidth() + 1];
				for (int i = 0; i < nums.length; i++) {
					// 将生成的每个随机数对应到要画的x轴上的某个坐标
					int x = (int) ((nums[i] - min) / width * card_frame.getWidth());
					fs[x] += 1;
				}
				// 记录最大频率，用于画y轴
				int f_max = 0;
				for (int i = 0; i < fs.length; i++) {
					if (fs[i] > f_max) {
						f_max = fs[i];
					}
				}
				// 画
				for (int i = 0; i < fs.length; i++) {
					// 将频率对应到要画的y轴上的某个坐标
					int y = (int) (card_frame.getHeight() / 2 - ((double) fs[i] / (double) f_max) * (card_frame.getHeight() / 2));
					graphics.drawLine(i, card_frame.getHeight() / 2, i, y);
				}
			}
		};
		// 做一个新panel，测试切换
		GridBagPanel panel2 = new GridBagPanel("panel2");
		JButton b = new JButton("2");
		panel2.addComponent(b, "button1", 1, 1, 1, 1, 1, 1, true);
		// 向frame中添加panel
		card_frame.addGridBagPanel(panel);
		card_frame.addGridBagPanel(panel2);
		// 添加点击切换事件
		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				card_frame.switchTo("panel2");
			}
		});
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				card_frame.switchTo(panel);
			}
		});
		// help
		JFrame help_frame = new JFrame("Help");
		card_frame.setHelpContents(help_frame);
		help_frame.setSize(200, 200);
		// about
		Dialog about_dialog = new Dialog(card_frame);
		card_frame.setAbout(about_dialog);
		about_dialog.setTitle("About");
		about_dialog.setSize(200, 200);
	}

	private double[] randomND(double mean, double variance, int size) {
		double[] result = new double[size];
		// 生成正态分布随机数
		NormalDistribution normal_dis = new NormalDistribution(mean, variance, null);
		for (int i = 0; i < result.length; i++) {
			result[i] = normal_dis.nextRandom();
		}
		// 打印平均值
		double sum = 0.0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
		}
		System.out.println("期望：\t" + mean + "\n方差：\t" + variance + "\n样本量：\t" + size + "\n平均值：\t" + sum / result.length);
		return result;
	}

	class SampleUnit implements Callable<String> {
		private String task_index;
		private GridBagPanel panel;
		private String name;

		SampleUnit(String task_index, GridBagPanel panel, String name) {
			this.task_index = task_index;
			this.panel = panel;
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			TextArea ta = (TextArea) panel.getComponent(name);
			long start_time = SystemTime.currentTimeMillis();
			ta.append("任务" + task_index + "于" + SystemTime.formatTime(start_time) + "启动\n");
			Thread.sleep(1000 * 3);
			long end_time = SystemTime.currentTimeMillis();
			ta.append("\t\t\t\t" + "任务" + task_index + "于" + SystemTime.formatTime(end_time) + "终止\n");
			return "任务" + task_index + "返回运行结果，执行时间为" + (end_time - start_time) + "毫秒";
		}
	}
}
