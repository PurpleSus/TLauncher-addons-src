var AbstractInsnNode = Java.type('org.objectweb.asm.tree.AbstractInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var playerClassName = 'player/AbstractClientPlayerEntity';
function AbstractClientPlayerTransformer() {
    return {
        'func_110303_q': function(cn, mn) {
            mn.instructions.clear();
			mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/" + playerClassName, "func_146103_bH", "()Lcom/mojang/authlib/GameProfile;"));
			mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "getLocationCape", "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/util/ResourceLocation;", false));
			mn.instructions.add(new InsnNode(Opcodes.ARETURN));
            print("Method func_110303_q hooked!");
        },
        'func_110306_p': function(cn, mn) {
			mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/" + playerClassName, "func_146103_bH", "()Lcom/mojang/authlib/GameProfile;"));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "getLocationSkin", "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/util/ResourceLocation;", false));
            mn.instructions.add(new InsnNode(Opcodes.ARETURN));
            print("Method func_110306_p hooked!");
        },
        'func_175154_l': function(cn, mn) {
			mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/" + playerClassName, "func_146103_bH", "()Lcom/mojang/authlib/GameProfile;"));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "getSkinType", "(Lcom/mojang/authlib/GameProfile;)Ljava/lang/String;", false));
            mn.instructions.add(new InsnNode(Opcodes.ARETURN));
            print("Method func_175154_l hooked!");
        },
        'func_184834_t': function(cn, mn) {
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/" + playerClassName, "func_146103_bH", "()Lcom/mojang/authlib/GameProfile;"));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "getLocationElytra", "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/util/ResourceLocation;", false));
            mn.instructions.add(new InsnNode(Opcodes.ARETURN));
            print("Method func_184834_t hooked!");
        }
    }
}

function initializeCoreMod() {
    return {
        'AbstractClientPlayerEntityTransformer': {
            'target': {
               'type': 'CLASS',
               'name': 'net/minecraft/client/entity/player/AbstractClientPlayerEntity'
            },
            "transformer": function (cn) {
                var a = 'func_110306_p';
                var b = 'func_110303_q';
                var c = 'func_175154_l';
                var d = 'func_184834_t';
                cn.methods.forEach(function (mn) {
                    if(mn.name === a)
                        AbstractClientPlayerTransformer()[a](cn, mn);
                    if(mn.name === b)
                        AbstractClientPlayerTransformer()[b](cn, mn);
                    if(mn.name === c)
                        AbstractClientPlayerTransformer()[c](cn, mn);
                    if(mn.name === d)
                        AbstractClientPlayerTransformer()[d](cn, mn);
                });
                return cn;
            }
        },
        'AbstractClientPlayerTransformer': {
            'target': {
               'type': 'CLASS',
               'name': 'net/minecraft/client/entity/AbstractClientPlayer'
            },
            "transformer": function (cn) {
                var a = 'func_110306_p';
                var b = 'func_110303_q';
                var c = 'func_175154_l';
                var d = 'func_184834_t';
                playerClassName = 'AbstractClientPlayer';
                cn.methods.forEach(function (mn) {
                    if(mn.name === a)
                        AbstractClientPlayerTransformer()[a](cn, mn);
                    if(mn.name === b)
                        AbstractClientPlayerTransformer()[b](cn, mn);
                    if(mn.name === c)
                        AbstractClientPlayerTransformer()[c](cn, mn);
                    if(mn.name === d)
                        AbstractClientPlayerTransformer()[d](cn, mn);
                });
                return cn;
            }
        },
        'PlayerEntityTransformer': {
            'target': {
               'type': 'CLASS',
               'name': 'net/minecraft/entity/player/PlayerEntity'
            },
            "transformer": function (cn) {
                cn.methods.forEach(function (mn) {
                    if(mn.name === 'func_175148_a') {
                        var list = new InsnList();
                        var labelNode = new LabelNode();
                        list.add(new VarInsnNode(Opcodes.ASTORE, 2));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/tweaker/hook/Hooks", "isCapePart", "(Ljava/lang/Enum;)Z", false));
                        list.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                        list.add(new InsnNode(Opcodes.ICONST_1));
                        list.add(new InsnNode(Opcodes.IRETURN));
                        list.add(labelNode);
                        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        mn.instructions.insertBefore(mn.instructions.getLast(), list);
                    }
                });
                return cn;
            }
        },
        'EntityPlayerTransformer': {
            'target': {
               'type': 'CLASS',
               'name': 'net/minecraft/entity/player/EntityPlayer'
            },
            "transformer": function (cn) {
                cn.methods.forEach(function (mn) {
                    if(mn.name === 'func_175148_a') {
                        var list = new InsnList();
                        var labelNode = new LabelNode();
                        list.add(new VarInsnNode(Opcodes.ASTORE, 2));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/tweaker/hook/Hooks", "isCapePart", "(Ljava/lang/Enum;)Z", false));
                        list.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                        list.add(new InsnNode(Opcodes.ICONST_1));
                        list.add(new InsnNode(Opcodes.IRETURN));
                        list.add(labelNode);
                        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        mn.instructions.insertBefore(mn.instructions.getLast(), list);
                    }
                });
                return cn;
            }
        },
        'GuiMainMenuTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/gui/GuiMainMenu'
            },
            "transformer": function (cn) {
                cn.methods.forEach(function (mn) {
                    if(mn.name === 'func_73863_a') {
                        mn.instructions.insertBefore(mn.instructions.get(0), new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "onMainMenuRender", "()V", false));
                        print("Method func_73863_a hooked!");
                    }
                });
                return cn;
            }
        },
        'ScreenMainMenuTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/gui/screen/MainMenuScreen'
            },
            "transformer": function (cn) {
                cn.methods.forEach(function (mn) {
                    if(mn.name === 'render' || mn.name === 'func_230430_a_') {
                        mn.instructions.insertBefore(mn.instructions.get(0), new MethodInsnNode(Opcodes.INVOKESTATIC, "org/tlauncher/TLSkinCape", "onMainMenuRender", "()V", false));
                        print("Method render hooked!");
                    }
                });
                return cn;
            }
        }
    };
}