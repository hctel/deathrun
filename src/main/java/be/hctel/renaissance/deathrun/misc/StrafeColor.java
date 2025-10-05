package be.hctel.renaissance.deathrun.misc;

public enum StrafeColor {
	WOOD("http://textures.minecraft.net/texture/f7aacad193e2226971ed95302dba433438be4644fbab5ebf818054061667fbe2", "http://textures.minecraft.net/texture/d34ef0638537222b20f480694dadc0f85fbe0759d581aa7fcdf2e43139377158", "http://textures.minecraft.net/texture/fe3d755cecbb13a39e8e9354823a9a02a01dce0aca68ffd42e3ea9a9d29e2df2"),
	GREEN("http://textures.minecraft.net/texture/f5347423ee55daa7923668fca8581985ff5389a45435321efad537af23d", "http://textures.minecraft.net/texture/4ef356ad2aa7b1678aecb88290e5fa5a3427e5e456ff42fb515690c67517b8", "http://textures.minecraft.net/texture/3b83bbccf4f0c86b12f6f79989d159454bf9281955d7e2411ce98c1b8aa38d8");	
	
	String leftTextureURL, rightTextureURL, backTextureURL;
	private StrafeColor(String leftTextureURL, String rightTextureURL, String backTextureURL) {
		this.leftTextureURL = leftTextureURL;
		this.rightTextureURL = rightTextureURL;
		this.backTextureURL = backTextureURL;
	}
	
	public String getLeftTextureURL() {
		return leftTextureURL;
	}
	
	public String getRightTextureURL() {
		return rightTextureURL;
	}
	
	public String getBackTextureURL() {
		return backTextureURL;
	}
	
	public String getDisabledLeftTextureURL() {
		return "http://textures.minecraft.net/texture/542fde8b82e8c1b8c22b22679983fe35cb76a79778429bdadabc397fd15061";
	}
	
	public String getDisabledRightTextureURL() {
		return "http://textures.minecraft.net/texture/406262af1d5f414c597055c22e39cce148e5edbec45559a2d6b88c8d67b92ea6";
	}
	
	public String getDisabledBackTextureURL() {
		return "http://textures.minecraft.net/texture/d1b62db5c0a3fa1ef441bf7044f511be58bedf9b6731853e50ce90cd44fb69";
	}	
}
