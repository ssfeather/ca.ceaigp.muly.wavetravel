package ca.ceaigp.muly.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigFile
{
	private String model = "iasp91";
	private String phases;
	private int[] depths;
	
	public String getPhases()
    {
    		return this.phases;
    }

	public int[] getDepths()
    {
		return this.depths;
    }

	public String getModel()
    {
    		return this.model;
    }

	public ReadConfigFile()
	{
		try
		{
			Properties props = new Properties();
			InputStream inputStream  = getClass().getResourceAsStream("/config.profile");  
			props.load(inputStream);
			//props.loadFromXML(inputStream);
            inputStream.close();
            
            model = props.getProperty("Model","iasp91");
            phases = props.getProperty("Phases");
            String[] strDepth = props.getProperty("Depths").split(",");
			depths = new int[strDepth.length];
			for(int i=0;i<strDepth.length;i++)
			{
				depths[i]=Integer.parseInt(strDepth[i]); 
				//System.out.println(depths[i]);
			}
			
			//System.out.println(model);
			//System.out.println(phases);
			//System.out.println(props.getProperty("Depths"));
            
            /*
             * 读取不确定参数的配置文件
             */
            /*
			Set<String> propertyNames = props.stringPropertyNames();
			for (String Property : propertyNames) 
			{
				if(Property.equals("Model"))
				{
					model = props.getProperty(Property);
					propertyNames.remove("Model");
				}
				else
				{
					//System.out.println(Property + ":" + props.getProperty(Property));
					String str[] = props.getProperty(Property).split(",");
					int depths[] = new int[str.length];
					for(int i=0;i<str.length;i++)
					{
						depths[i]=Integer.parseInt(str[i]); 
					}
					phaseMap.put(Property, depths);
				}
			}
			
			propertyNames = phaseMap.keySet();
			for(String Property : propertyNames)
			{
				System.out.println(Property + ":" + phaseMap.get(Property).length);
			}
			*/

		}
		catch (FileNotFoundException e) 
		{
			 e.printStackTrace();
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
		} 
		catch (Exception e) 
		{
			 e.printStackTrace();
		}
	}
}
